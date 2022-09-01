//
//  IPayViewController.m
//  wander_i_pay88
//
//  Created by Lakshman Gurung on 02/09/22.
//

#import "IPayViewController.h"

@interface IPayViewController ()

@end

@implementation IPayViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self addBackButton];
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
}


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
