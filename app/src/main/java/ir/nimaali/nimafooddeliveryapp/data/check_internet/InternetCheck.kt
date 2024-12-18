package ir.nimaali.nimafooddeliveryapp.data.check_internet

import android.content.Context
import com.android.volley.toolbox.StringRequest
import ir.nimaali.nimafooddeliveryapp.data.VolleySingleTon

class InternetCheck(val m_context:Context) {
    fun checkConnection(success:()->Unit,failed:()->Unit){
        val str_request =
            object : StringRequest(
                Method.GET, "https://www.google.com/" ,
                { strResponse ->
                    success()
                }
                //error
                , {
                    failed()
                }) {
            }//end request register
        VolleySingleTon.getInstance(m_context).addToRequestQueue(str_request)
    }
}