(function() {
   function call(params){
     prompt('kjtpayApp://{\"type\":\"'+params+'\"}');
   };

 function call(type,params){
   if(params == undefined){
     prompt('kjtpayApp://{\"type\":\"'+type+'\"}');
   } else {
     prompt('kjtpayApp://{\"type\":\"'+type+'\",\"params\":'+ params +'}');
   }
 };

   window.sendMessageToApp = {};
   sendMessageToApp.call = call;
})();