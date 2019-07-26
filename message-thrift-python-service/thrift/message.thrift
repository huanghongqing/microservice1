namespace java com.imooc.thrift.message
namespace py message.api

service MessageService {
    bool sendMobileMessage(1:string mobile, 2:string message);
    bool sendMail(1:string email, 2:string message);
}