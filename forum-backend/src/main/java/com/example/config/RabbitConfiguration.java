package com.example.config;

import com.example.utils.Const;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ消息队列配置
 */
@Configuration
public class RabbitConfiguration {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean("errorQueue")
    public Queue dlqueue() {
        return QueueBuilder
                .durable(Const.MQ_ERROR)
                .ttl(24 * 60 *60 * 1000)
                .build();
    }

    @Bean("errorExchange")
    public Exchange dlexchange() {
        return ExchangeBuilder
                .directExchange("dlx.direct")
                .build();
    }

    @Bean
    public Binding dlbinding(@Qualifier("errorExchange") Exchange exchange, @Qualifier("errorQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(Const.MQ_ERROR)
                .noargs();
    }


    @Bean("mailQueue")
    public Queue queue() {
        return QueueBuilder
                .durable(Const.MQ_MAIL)
                .deadLetterExchange("dlx.direct")
                .deadLetterRoutingKey(Const.MQ_ERROR)
                .ttl(3 * 60 * 1000)
                .build();
    }
}
