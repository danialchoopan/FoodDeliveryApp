package ir.nimaali.nimafooddeliveryapp.data

object RequestEndPoints {
    val rootDomain = "http://192.168.1.11:5000"
    val rootDomainApi = "$rootDomain"
//    val storageLoad = "$rootDomain/storage/"

    //user auth requests
    val userRegister = "$rootDomainApi/user/register"
    val userLogin = "$rootDomainApi/user/login"
    val userEditPassword = "$rootDomainApi/user/edit/password"
    val userEditCityName = "$rootDomainApi/user/edit/city"

    //user auth requests
    val sellerRegister = "$rootDomainApi/seller/register"
    val sellerLogin = "$rootDomainApi/seller/login"
    val sellerEditPassword = "$rootDomainApi/seller/edit/password"
    val sellerEditCityName = "$rootDomainApi/seller/edit/city"


    //user restaurants
    val userRestaurants = "$rootDomainApi/user/restaurants"


}