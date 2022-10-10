//
//  IPayViewController.m
//  wander_i_pay88
//
//  Created by Lakshman Gurung on 02/09/22.
//

#import "IPayViewController.h"

int const kTimeoutInMinutes = 25;
@interface IPayViewController ()

@end

@implementation IPayViewController

NSTimer *autoCloseTimer;

- (void)viewDidLoad {
    [super viewDidLoad];
    [self addBackButton];
    [self setupAutoClose];
    // Do any additional setup after loading the view.
}

-(void) addBackButton {
    NSLog(@"addBackButton invoked");
    
    [self.navigationController.navigationBar setBarTintColor:[UIColor whiteColor]];
    [self.navigationController.barHideOnSwipeGestureRecognizer setEnabled:NO];
    [self.navigationController.barHideOnTapGestureRecognizer setEnabled:NO];
    UIBarButtonItem* backBtn = [[UIBarButtonItem alloc] initWithTitle:@"Close" style:UIBarButtonItemStyleDone target:self action:@selector(onBack)];
    [backBtn setTintColor:[UIColor blackColor]];
    
    [self.navigationItem setLeftBarButtonItem:backBtn];
}

-(void) onBack {
    if(self.onClose != nil){
        self.onClose();
    }
    
    [self.navigationController dismissViewControllerAnimated:true completion:nil];
    [autoCloseTimer invalidate];
}


-(void) setupAutoClose {

    int delayInMinutes = kTimeoutInMinutes;
    int timeout = [_timeoutInMinutes intValue];
    if(timeout > 0) {
        delayInMinutes = timeout;
    }
    
    int delayInSeconds = delayInMinutes*60;
    autoCloseTimer = [NSTimer scheduledTimerWithTimeInterval:delayInSeconds
             target:self
             selector:@selector(autoClose)
             userInfo:nil
             repeats:NO];
}

-(void) autoClose {
    if(self.onTimeout != nil){
        self.onTimeout();
    }
    
    [self.navigationController dismissViewControllerAnimated:true completion:nil];
    [autoCloseTimer invalidate];
}
@end
