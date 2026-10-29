module jmp.cloud.service.impl {
    requires transitive jmp.service.api;
    requires jmp.dto;

    exports com.jmp.cloud.service.impl;

    provides com.jmp.service.api.Service with com.jmp.cloud.service.impl.ServiceImpl;
}
