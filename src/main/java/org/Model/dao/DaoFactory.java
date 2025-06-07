package org.Model.dao;

import org.Model.dao.impl.DepartamentJDBC;
import org.Model.dao.impl.SellerJDBC;

public class DaoFactory {

    public DepartamentJDBC departamentDao(){
        return  new DepartamentJDBC();
    }

    public SellerJDBC sellerJDBC(){
        return new SellerJDBC();
    }

}
