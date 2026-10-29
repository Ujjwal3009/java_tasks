module jmp.application {
    requires jmp.dto;
    requires jmp.bank.api;
    requires jmp.service.api;
    requires jmp.cloud.bank.impl;
    requires jmp.cloud.service.impl;

    uses com.jmp.bank.api.Bank;
    uses com.jmp.service.api.Service;

    exports com.jmp.application;
}
