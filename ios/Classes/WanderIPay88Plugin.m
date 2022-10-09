#import "WanderIPay88Plugin.h"
static FlutterMethodChannel* channel;

NSString *const kPaymentSucceeded = @"onPaymentSucceeded";
NSString *const kPaymentCanceled = @"onPaymentCanceled";
NSString *const kPaymentFailed = @"onPaymentFailed";
@implementation WanderIPay88Plugin
UIView* _paymentView;
UINavigationController* _navVC;
-(id) init {
    self = [super init];
    paymentSdk = [[Ipay alloc] init];
    paymentSdk.delegate = self;
    return self;
}

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  channel = [FlutterMethodChannel
      methodChannelWithName:@"today.wander.acs.ipay88"
            binaryMessenger:[registrar messenger]];
  WanderIPay88Plugin* instance = [[WanderIPay88Plugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  } else if([call.method isEqualToString:@"checkout"]) {
      result(nil);
      if (![call.arguments isKindOfClass:[NSDictionary class]]) {
          return;
      }
      NSDictionary* arguments = call.arguments;
      id merchantCode = [arguments objectForKey:@"merchantCode"];
      id paymentId = [arguments objectForKey:@"paymentId"];
      id refNo = [arguments objectForKey:@"refNo"];
      id amount = [arguments objectForKey:@"amount"];
      id currency = [arguments objectForKey:@"currency"];
      id prodDesc = [arguments objectForKey:@"prodDesc"];
      id userName = [arguments objectForKey:@"username"];
      id userEmail = [arguments objectForKey:@"userEmail"];
      id userContact = [arguments objectForKey:@"userContact"];
      id remark = [arguments objectForKey:@"remark"];
      id lang = [arguments objectForKey:@"lang"];
      id country = [arguments objectForKey:@"country"];
      id backendPostURL = [arguments objectForKey:@"backendPostURL"];
      id appDeepLink = [arguments objectForKey:@"appDeepLink"];
      
      
      IpayPayment* payment = [[IpayPayment alloc] init];
      [payment setMerchantCode:merchantCode ? [merchantCode description] : @""];
      [payment setPaymentId:paymentId ? [paymentId description] : @""];
      [payment setRefNo:refNo ? [refNo description] : @""];
      [payment setAmount:amount ? [amount description] : @"1.00"];
      [payment setCurrency:currency ? [currency description] : @"MYR"];
      [payment setProdDesc:prodDesc ? [prodDesc description] : @""];
      [payment setUserName:userName ? [userName description] : @""];
      [payment setUserEmail:userEmail ? [userEmail description] : @""];
      [payment setUserContact:userContact ? [userContact description] : @""];
      [payment setRemark:remark ? [remark description] : @""];
      [payment setLang:lang ? [lang description] : @""];
      [payment setCountry:country ? [country description] : @"MY"];
      [payment setBackendPostURL:backendPostURL ? [backendPostURL description] : @""];
      if (appDeepLink) {
          [payment setAppdeeplink:appDeepLink];
      }
      
     _paymentView = [paymentSdk checkout:payment];
      if (_paymentView != nil) {
          UIViewController* rootController = [[[UIApplication.sharedApplication delegate] window] rootViewController];
        
          IPayViewController *controller = [[IPayViewController alloc] init];
          [[controller view] addSubview:_paymentView];
          
          
          [controller setOnClose:^{
              [self cancelPayment:refNo withTransId:@"" withAmount:amount withRemark:@"Payment Canceled" withErrDesc:@"Payment Canceled by user."];
          }];
          
          _navVC = [[UINavigationController alloc] initWithRootViewController:controller];
          [_navVC setModalPresentationStyle:UIModalPresentationFullScreen];
          [rootController presentViewController:_navVC animated:true completion:nil];
          
      }
  } else if ([call.method isEqualToString:@"requery"]) {
      result(nil);
      if (![call.arguments isKindOfClass:[NSDictionary class]]) {
          return;
      }
      NSDictionary* arguments = call.arguments;
      id merchantCode = [arguments objectForKey:@"merchantCode"];
      id refNo = [arguments objectForKey:@"refNo"];
      id amount = [arguments objectForKey:@"amount"];
      IpayPayment* payment = [[IpayPayment alloc] init];
      [payment setMerchantCode:merchantCode ? [merchantCode description] : @""];
      [payment setRefNo:refNo ? [refNo description] : @""];
      [payment setAmount:amount ? [amount description] : @"1.00"];
      [paymentSdk requery:payment];
  }   else {
    result(FlutterMethodNotImplemented);
  }
}

- (void)paymentSuccess:(NSString *)refNo withTransId:(NSString *)transId withAmount:(NSString *)amount withRemark:(NSString *)remark withAuthCode:(NSString *)authCode {
    if(_navVC != NULL) {
        [_navVC dismissViewControllerAnimated:true completion:false];
    }
    [self successResponse];
    return;
    [channel invokeMethod:kPaymentSucceeded arguments:@{
        @"transId": transId,
        @"refNo": refNo,
        @"amount": amount,
        @"remark": remark,
        @"authCode": authCode
    }];
}

- (void)paymentCancelled:(NSString *)refNo withTransId:(NSString *)transId withAmount:(NSString *)amount withRemark:(NSString *)remark withErrDesc:(NSString *)errDesc {
    if(_navVC != NULL) {
        [_navVC dismissViewControllerAnimated:true completion:false];
    }
    [self successResponse];
    return;
    [channel invokeMethod:kPaymentCanceled arguments: @{
        @"transId": transId,
        @"refNo": refNo,
        @"amount": amount,
        @"remark": remark,
        @"errDesc": errDesc
    }];
}

- (void)requeryFailed:(NSString *)refNo withMerchantCode:(NSString *)merchantCode withAmount:(NSString *)amount withErrDesc:(NSString *)errDesc {
    [channel invokeMethod:@"onRequeryResult" arguments: @{
        @"merchantCode": merchantCode,
        @"refNo": refNo,
        @"amount": amount,
        @"errDesc": errDesc
    }];
}

- (void)paymentFailed:(NSString *)refNo withTransId:(NSString *)transId withAmount:(NSString *)amount withRemark:(NSString *)remark withErrDesc:(NSString *)errDesc {
    if(_navVC != NULL) {
        [_navVC dismissViewControllerAnimated:true completion:false];
    }
    [self successResponse];
    return;
    [channel invokeMethod:kPaymentFailed arguments: @{
        @"transId": transId,
        @"refNo": refNo,
        @"amount": amount,
        @"remark": remark,
        @"errDesc": errDesc
    }];
}


- (void)requerySuccess:(NSString *)refNo withMerchantCode:(NSString *)merchantCode withAmount:(NSString *)amount withResult:(NSString *)result {
    [channel invokeMethod:@"onRequeryResult" arguments: @{
        @"merchantCode": merchantCode,
        @"refNo": refNo,
        @"amount": amount,
        @"result": result
    }];
}

-(void) cancelPayment:(NSString *)refNo withTransId:(NSString *)transId withAmount:(NSString *)amount withRemark:(NSString *)remark withErrDesc:(NSString *)errDesc {
    NSLog(@"cancelPayment test ----------------->");
    [self successResponse];
    return;
    [channel invokeMethod:kPaymentSucceeded arguments: @{
        @"transId": transId,
        @"refNo": refNo,
        @"amount":amount,
        @"remark": remark,
        @"errDesc": errDesc,
        @"authCode": @"AW-101",
    }];
}


-(void) successResponse {
    NSLog(@"cancelPayment test ----------------->");
    [channel invokeMethod:kPaymentSucceeded arguments: @{
        @"transId": @"",
        @"refNo": @"",
        @"amount":@"1.0",
        @"remark": @"",
        @"errDesc": @"",
        @"authCode": @"AW-101",
    }];
}
@end

