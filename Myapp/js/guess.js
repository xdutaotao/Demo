/**
 *
 * Description:
 *
 * Created by GUZHENFU on 2017/5/8.
 */


/*****************GetQuizChanceTimesTodayRest******************success***********/

function getTodayResultNum() {
    
    var traderID=GetQueryString("traderID");
    
    var paramTest = {
        "MethodName":"GetQuizChanceTimesTodayRest",
        "ServiceName":"Shcem.Member.ServiceContract.IQuizService",
        "Params":"{\"TraderID\":" + traderID + "}"
    };
    jsonAjax(paramTest,callbackTest,errorbackTest);


    function callbackTest(data){
        var objtest = JSON.parse(data.DATA);

        $(".guess-result").html(objtest);
    };
    function errorbackTest(data){
        webToast("网络请求失败","middle",1000);
    }
}
