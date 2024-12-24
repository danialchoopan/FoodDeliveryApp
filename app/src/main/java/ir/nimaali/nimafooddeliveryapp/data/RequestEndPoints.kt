package ir.nimaali.nimafooddeliveryapp.data

object RequestEndPoints {
    val rootDomain = "http://192.168.1.11:5000"
    val rootDomainApi = "$rootDomain"
    val storageImageLoadRestaurants = "$rootDomain/static/images/restaurants"
    val storageImageLoadFood = "$rootDomain/static/images/food"

    //user auth requests
    val userRegister = "$rootDomainApi/user/register"
    val userLogin = "$rootDomainApi/user/login"
    val userEditPassword = "$rootDomainApi/user/edit/password"
    val userEditCityName = "$rootDomainApi/user/edit/city"

    val showUserOrderData="$rootDomainApi/user/orders/total"


    //user auth requests
    val sellerRegister = "$rootDomainApi/seller/register"
    val sellerLogin = "$rootDomainApi/seller/login"
    val sellerEditPassword = "$rootDomainApi/seller/edit/password"
    val sellerEditCityName = "$rootDomainApi/seller/edit/city"


    //user restaurants
    val userRestaurants = "$rootDomainApi/user/restaurants"
    val userRestaurantsDetail = "$rootDomainApi/user/restaurant"

    //order
    val placeOrderUser="$rootDomainApi/add/order"
    val usersOrdersShow="$rootDomainApi/user/orders"
    val usersOrdersShowAll="$rootDomainApi/user/orders/all"
    val usersOrderCancel="$rootDomainApi/user/orders/cancel"


    val userAddCommentRestaurantsDetail = "$rootDomainApi/add/comment"
    val userDeleteCommentRestaurantsDetail = "$rootDomainApi/comment/delete"



    //seller foods
    val sellerFood = "$rootDomainApi/seller/foods"
    val sellerFoodEditGet = "$rootDomainApi/seller/food"

    //seller dash
    val sellerOpenStatus = "$rootDomainApi/seller/open/status"
    val sellerOpen = "$rootDomainApi/seller/open"

    //seller order
    val sellerShowOrderDash="$rootDomainApi/seller/orders/not"
    val sellerShowOrderDashAll="$rootDomainApi/seller/orders/all"

    val sellerShowOrderById="$rootDomainApi/seller/orders/detail"

    val sellerOrderPreparing="$rootDomainApi/seller/orders/preparing"
    val sellerOrderCompleted="$rootDomainApi/seller/orders/completed"
    val sellerOrderCancel="$rootDomainApi/seller/orders/cancel"


    val sellerTotalOrder="$rootDomainApi/seller/orders/total"


}