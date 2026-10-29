module jmp.cloud.bank.impl {
    requires transitive jmp.bank.api;
    requires jmp.dto;

    exports com.jmp.cloud.bank.impl;

    provides com.jmp.bank.api.Bank with com.jmp.cloud.bank.impl.BankImpl;
}
