# coding :utf-8
from api import  MessageService
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
import  smtplib
from email.mime.text import   MIMEText
from email.header import  Header

sender='imoocd@163.com'
authCode='aA111111'
class messageServiceHandler :
    def sendMobileMessage(self, mobile, message):
            print  "sendmobilemessage  "+mobile
            return True

    def sendMail(self, email, message):
            print "sendemailmessage  "+email
            messageobj=MIMEText(message,"plain","utf-8")
            messageobj['From'] = sender
            messageobj['To']=email
            messageobj['Subject']=Header('Akin Test email','utf-8')
            try:
                    smtpobj=smtplib.SMTP('smtp.163.com')
                    smtpobj.login(sender,authCode)
                    smtpobj.sendmail(sender,[email],messageobj.as_string())
            except smtplib.SMTPException, ex:
                print "exception"
                print ex
                return False
            print 'send email success'
            return True

if __name__== '__main__':
    handler = messageServiceHandler()
    processor = MessageService.Processor(handler)
    transport = TSocket.TServerSocket("localhost", "9090")
    tfactory = TTransport.TFramedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor,transport,tfactory,pfactory)
    print  "start python2 version"
    server.serve()
    print "exit"
