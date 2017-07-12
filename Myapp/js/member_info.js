/**
 * Created by lijianhui on 2017.2.22.
 */

//获取url
function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
var FirmId=GetQueryString("ID");
$.getJSON("https://fms.uat.shcem.com/mapi/file/dynamicimage?id=1086",function(data){
    console.log(data)
})
//调用数据
var Memberparams = {
    "MethodName":"GetUserAuthenticationDetail",
    "ServiceName":"Shcem.Member.ServiceContract.IUserAuthenticationService",
    "Params":"{\"FirmRegId\":\"\",\"FirmId\":"+FirmId+"}",
    "authkeyid":"t_testuse"
}
jsonAjax(Memberparams,Membercallback,Membererrorback);
//调取数据成功
function Membercallback(data) {
    if(data.CODE == "MSG00000"){
    var Memberobj=JSON.parse(data.DATA);
    //判断用户的审核状态
    if(Memberobj.FirmStatus == 2)
    {//审核通过
        $(".title-tip").html("审核通过，如需要更新三证或补充危化证信息，请联系客服<a href='tel:4007-209-209'>4007-209-209</a>")
    }else if(Memberobj.FirmStatus == 1)
    {//审核不通过
        $(".title-tip").html("您好！您所提交的资料未通过审核，请修改信息后重新提交，谢谢您的配合！<div class='cus-red'>驳回理由：Memberobj.RejectReason</div>")
    }else{//审核中
        $(".title-tip").html("审核中，如需要更新三证或补充危化证信息，请联系客服<a href='tel:4007-209-209'>4007-209-209</a>")
    }
    //判断是否填写银行信息和开票信息
    if(Memberobj.SettingBankID == null)
    {//未绑定签约银行
        $(".bank-info").hide();
    }
    if(Memberobj.VatBankID == null)
    {//未绑定开票信息
        $(".bank-ticket").hide();
    }
    // 页面元素赋值
    $(".member-node").html(Memberobj.FirmID);//交易商编码
    $(".member-company").html(Memberobj.FirmName);//公司名称
    $(".member-address").html((Memberobj.CantonFullDescription)+"&nbsp;"+(Memberobj.Address));//详细地址
    $(".member-phone").html(Memberobj.ContactTelAreaNo);//座机
    $(".member-people").html(Memberobj.ContactName);//联系人
    $(".member-tel").html(Memberobj.ContactMobile);//手机号
    $(".member-email").html(Memberobj.ContactEmail);//邮箱
    //银行信息
    $(".member-deal").html((Memberobj.SettingBankName)+"-"+(Memberobj.SettingBkBranch));//签约银行
    $(".member-num").html(Memberobj.SettingBkAccount);//银行账号
    $(".member-bank").html(Memberobj.VatBankName);//银行账号
    $(".member-ticket").html(Memberobj.VatBkAccount);//开票账号
    $(".member-tax").html(Memberobj.TaxNo);//企业税号
    $(".member-tax-tel").html(Memberobj.VatContactTelNo);//开票电话
    $(".member-tax-address").html((Memberobj.VatProvinceName)+"-"+(Memberobj.VatCityName)+"-"+(Memberobj.VatAreaName)+"-"+            (Memberobj.VatAddress));//开票地址
    //判断是图片还是PDF
    if(Memberobj.LicenseDiffer > 0)
    {//综合证
        $(".stevens-jonnson").show();
        $.getJSON("https://fms.uat.shcem.com/mapi/file/fileinfos?ids="+(Memberobj.LicenseDiffer),function(data){
            var Imgsrc=data.data[0]
            var suffix = Imgsrc.split('.')[Imgsrc.split('.').length - 1];
            console.log(suffix);
        if(suffix.toUpperCase() == 'PDF'){
            $(".stevens-jonnson a").attr("href",(downloadUrl)+(Memberobj.LicenseDiffer))
            $(".stevens-jonnson img").attr("src",memberUrl+"#id=download");
        }else{
            $(".stevens-jonnson a").attr("href","#")
            $(".stevens-jonnson img").attr("src",(downloadUrl)+(Memberobj.LicenseDiffer))
        }
        })

    }
    else
    {
        if(Memberobj.BSLicense > 0)
        {//营业执照
            $(".business-license").show();
            $.getJSON("https://fms.uat.shcem.com/mapi/file/fileinfos?ids="+(Memberobj.BSLicense),function(data){
                var Imgsrc=data.data[0]
                var suffix = Imgsrc.split('.')[Imgsrc.split('.').length - 1];
                console.log(suffix);
                if(suffix.toUpperCase() == 'PDF'){
                    $(".business-license a").attr("href",(downloadUrl)+(Memberobj.BSLicense))
                    $(".business-license img").attr("src",memberUrl+"#id=download");
                }else{
                    $(".business-license a").attr("href","#")
                    $(".business-license img").attr("src",(downloadUrl)+(Memberobj.BSLicense))
                }
            })
        }
        if(Memberobj.TaxLicense > 0)
        {//税务登记证
            $(".tax-login").show();
            $.getJSON("https://fms.uat.shcem.com/mapi/file/fileinfos?ids="+(Memberobj.TaxLicense),function(data){
                var Imgsrc=data.data[0]
                var suffix = Imgsrc.split('.')[Imgsrc.split('.').length - 1];
                console.log(suffix);
                if(suffix.toUpperCase() == 'PDF'){
                    $(".tax-login a").attr("href",(downloadUrl)+(Memberobj.TaxLicense))
                    $(".tax-login img").attr("src",memberUrl+"#id=download");
                }else{
                    $(".tax-login a").attr("href","#")
                    $(".tax-login img").attr("src",(downloadUrl)+(Memberobj.TaxLicense))
                }
            })
        }
        if(Memberobj.OrgCode > 0)
        {//组织机构代码证
            $(".organize-code").show();
            $.getJSON("https://fms.uat.shcem.com/mapi/file/fileinfos?ids="+(Memberobj.OrgCode),function(data){
                var Imgsrc=data.data[0]
                var suffix = Imgsrc.split('.')[Imgsrc.split('.').length - 1];
                console.log(suffix);
                if(suffix.toUpperCase() == 'PDF'){
                    $(".organize-code a").attr("href",(downloadUrl)+(Memberobj.OrgCode))
                    $(".organize-code img").attr("src",memberUrl+"#id=download");
                }else{
                    $(".organize-code a").attr("href","#")
                    $(".organize-code img").attr("src",(downloadUrl)+(Memberobj.OrgCode))
                }
            })
        }
    }
    if(Memberobj.DGLicense > 0)
    {//危化品经营许可证
        $(".danger-license").show();
        $.getJSON("https://fms.uat.shcem.com/mapi/file/fileinfos?ids="+(Memberobj.DGLicense),function(data){
            var Imgsrc=data.data[0]
            var suffix = Imgsrc.split('.')[Imgsrc.split('.').length - 1];
            console.log(suffix);
            if(suffix.toUpperCase() == 'PDF'){
                $(".danger-license a").attr("href",(downloadUrl)+(Memberobj.DGLicense))
                $(".danger-license img").attr("src",memberUrl+"#id=download");
            }else{
                $(".danger-license a").attr("href","#")
                $(".danger-license img").attr("src",(downloadUrl)+(Memberobj.DGLicense))
            }
        })

    }
    }else if(data.CODE.indexOf("10012") >= 0){
        window.location.href='key?ID='+-1;
     }
}
//调用数据失败
function Membererrorback(data) {
    webToast("网络请求失败","middle",1000);
}