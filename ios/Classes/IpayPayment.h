//
//  IpayPayment.h
//  Pods
//
//  Created by Lakshman Gurung on 27/08/22.
//
#import <Foundation/Foundation.h>
#ifndef IpayPayment_h
#define IpayPayment_h


@interface IpayPayment : NSObject {
    NSString *merchantCode;
    NSString *paymentId;
    NSString *refNo;
    NSString *amount;
    NSString *currency;
    NSString *prodDesc;
    NSString *userName;
    NSString *userEmail;
    NSString *userContact;
    NSString *remark;
    NSString *lang;
    NSString *country;
    NSString *backendPostURL;
    NSString *appdeeplink;
}

@property (nonatomic, retain) NSString *merchantCode;
@property (nonatomic, retain) NSString *paymentId;
@property (nonatomic, retain) NSString *refNo;
@property (nonatomic, retain) NSString *amount;
@property (nonatomic, retain) NSString *currency;
@property (nonatomic, retain) NSString *prodDesc;
@property (nonatomic, retain) NSString *userName;
@property (nonatomic, retain) NSString *userEmail;
@property (nonatomic, retain) NSString *userContact;
@property (nonatomic, retain) NSString *remark;
@property (nonatomic, retain) NSString *lang;
@property (nonatomic, retain) NSString *country;
@property (nonatomic, retain) NSString *backendPostURL;
@property (nonatomic, retain) NSString *appdeeplink;

@end


#endif /* IpayPayment_h */
