package com.dpdc.realestate.service;

import com.dpdc.realestate.dto.response.BillDTO;
import com.dpdc.realestate.models.entity.CustomerPackageRegistration;

public interface RegisterPackageService {


    BillDTO checkBill (Integer packageId, Integer customerId, Integer quantity);

    CustomerPackageRegistration getCustomerPackageRegistration(Integer packId,
                                                               Integer customerId,
                                                               Integer quantity);

    CustomerPackageRegistration  savePackageRegister( Integer packageId,Integer customerId,Integer quantity);

}
