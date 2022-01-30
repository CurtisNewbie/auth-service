package com.curtisnewbie.service.auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingResult;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingStatus;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Events that need to be handled by someone, e.g., administrators
 *
 * @author yongjie.zhuang
 */
@Data
@TableName("event_handling")
public class EventHandling {

    /** primary key */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** type of event, 1-registration */
    @TableField("type")
    private EventHandlingType type;

    /** body of the event */
    @TableField("body")
    private String body;

    /** status of event, 0-no need to handle, 1-to be handled, 2-handled */
    @TableField("status")
    private EventHandlingStatus status;

    /** handle result, 1-accept, 2-reject */
    @TableField("handle_result")
    private EventHandlingResult handleResult;

    /** id of user who handled the event */
    @TableField("handler_id")
    private Integer handlerId;

    /** when the event is handled */
    @TableField("handler_time")
    private LocalDateTime handleTime;

}