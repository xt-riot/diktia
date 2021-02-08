package com.riot;

public enum Responses {
    Client_Request_LogIn,
    Client_Request_Exit,
    Client_Request_MailBox,
    Client_Request_NewEmail,
    Client_Request_ReadEmail,
    Client_Request_DeleteEmail,
    Client_Request_LogOut,
    Client_Request_Register,
    Server_Response_UserNameExists,
    Server_Response_InvalidUsernameOrPassword,
    Server_Response_InvalidReceiver,
    Server_Response_InvalidEmailID,
    Server_Response_UserLoggedIn,
    Server_Response_UserLoggedOut,
    Server_Response_Ready,
    Server_Response_NoUser,
    Server_Response_EmailSent,
    Server_Response_MailBox,
    Server_Response_ReadEmail,
    Server_Response_EmailDeleted
}
