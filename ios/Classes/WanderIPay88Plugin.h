#import <Flutter/Flutter.h>
#import "Ipay.h"
@interface WanderIPay88Plugin : NSObject<FlutterPlugin, PaymentResultDelegate> {
    Ipay* paymentSdk;
}
@end
