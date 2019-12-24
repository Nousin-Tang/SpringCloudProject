package com.nousin.springcloud.server.storage.framework.security.model;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO
 *
 * @author Nousin
 * @since 2019/12/24
 */
@Getter
@Setter
public class Permission {
    private String id;
    private String url;
    private String permission;
}
