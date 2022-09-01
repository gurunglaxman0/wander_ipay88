//
//  IPayViewController.h
//  wander_i_pay88
//
//  Created by Lakshman Gurung on 02/09/22.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

@interface IPayViewController : UIViewController

@property(strong) void (^onClose)(void);


@end

