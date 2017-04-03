package com.beiyelin.shop.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by newmann on 2017/3/29.
 */
@Configuration
@EnableSwagger2
@EnableWebMvc //必须存在
@ComponentScan(basePackages="com.beiyelin.shop.modules.sys.api")
public class SwaggerConfig {
//    private SpringSwaggerConfig springSwaggerConfig;
//    /**
//     * Required to autowire SpringSwaggerConfig
//     */
//    @Autowired
//    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig)
//    {
//        this.springSwaggerConfig = springSwaggerConfig;
//    }

    /**
     * Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc
     * framework - allowing for multiple swagger groups i.e. same code base
     * multiple swagger resource listings.
     */
//    @Bean
//    public SwaggerSpringMvcPlugin customImplementation()
//    {
//        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
//                .apiInfo(apiInfo())
//                .includePatterns(".*?");
//    }
//    private ApiInfo apiInfo()
//    {
//        ApiInfo apiInfo = new ApiInfo(
//                "Beiyelin Backend Restful API",
//                "后台Restful API 定义",
//                "My Apps API terms of service",
//                "My Apps API Contact Email",
//                "My Apps API Licence Type",
//                "My Apps API License URL");
//        return apiInfo;
//    }

    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .forCodeGeneration(true)
//                .groupName("outer api")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        //        ApiInfo apiInfo = new ApiInfo(
        //                "My Project's REST API",
        //                "This is a description of your API.",
        //                "version:1.0",
        //                "API TOS",
        //                "me@wherever.com",
        //                "API License",
        //                "API License URL"
        //        );
        Contact contact=new Contact("NewmannHU","http://www.baidu.com","newmannhu@qq.com");
        ApiInfo apiInfo = new ApiInfoBuilder().license("Apache License Version 2.0")
                .title("Beiyelin Backstone API")
                .description("后台系统Restful API接口定义")
                .contact(contact)
                .version("0.1")
                .build();

        return apiInfo;
    }
    @Bean
    UiConfiguration uiConfig() {
        return new UiConfiguration(
                "validatorUrl",// url
                "none",       // docExpansion          => none | list
                "alpha",      // apiSorter             => alpha
                "schema",     // defaultModelRendering => schema
                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS,
                false,        // enableJsonEditor      => true | false
                true,         // showRequestHeaders    => true | false
                60000L);      // requestTimeout => in milliseconds, defaults to null (uses jquery xh timeout)
    }

}
