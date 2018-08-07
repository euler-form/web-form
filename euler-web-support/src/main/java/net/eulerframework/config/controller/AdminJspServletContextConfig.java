packages = { "**.web.**.controller.admin" }, 
        useDefaultFilters = false, 
        includeFilters = @ComponentScan.Filter(JspController.class),
        excludeFilters = @ComponentScan.Filter(
                type=FilterType.ASPECTJ, 
                pattern={
                        "*..web..controller.admin.ajax..*"
                        })
)
@ImportResource({"classpath*:config/controller-security.xml"})
public class AdminJspServletContextConfig implements WebMvcConfigurer  {
    
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);

        resolver.setPrefix(WebConfig.getAdminJspPath());
        resolver.setSuffix(".jsp");
        return resolver;
    }
    
    @Resource
    private SpringValidatorAdapter validator;
    
    @Override
    public Validator getValidator() {
        return this.validator;
    }

    @Resource(name = "objectMapper")
    ObjectMapper objectMapper;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new SourceHttpMessageConverter<>());

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(
                Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.valueOf("text/json;charset=UTF-8")));
        jsonConverter.setObjectMapper(this.objectMapper);
        converters.add(jsonConverter);

    }
    
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        Map<String, MediaType> mediaTypes = new HashMap<>();
        ;
        mediaTypes.put("json", MediaType.APPLICATION_JSON_UTF8);

        configurer.favorPathExtension(false).favorParameter(false).ignoreAcceptHeader(false)
                .defaultContentType(MediaType.APPLICATION_JSON_UTF8).mediaTypes(mediaTypes);
    }
}
