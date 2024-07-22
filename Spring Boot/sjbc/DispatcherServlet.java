//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.web.servlet;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.ui.context.ThemeSource;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.util.ServletRequestPathUtils;
import org.springframework.web.util.WebUtils;

public class DispatcherServlet extends FrameworkServlet {
    public static final String MULTIPART_RESOLVER_BEAN_NAME = "multipartResolver";
    public static final String LOCALE_RESOLVER_BEAN_NAME = "localeResolver";
    /** @deprecated */
    @Deprecated
    public static final String THEME_RESOLVER_BEAN_NAME = "themeResolver";
    public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";
    public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";
    public static final String HANDLER_EXCEPTION_RESOLVER_BEAN_NAME = "handlerExceptionResolver";
    public static final String REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME = "viewNameTranslator";
    public static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";
    public static final String FLASH_MAP_MANAGER_BEAN_NAME = "flashMapManager";
    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";
    public static final String LOCALE_RESOLVER_ATTRIBUTE = DispatcherServlet.class.getName() + ".LOCALE_RESOLVER";
    /** @deprecated */
    @Deprecated
    public static final String THEME_RESOLVER_ATTRIBUTE = DispatcherServlet.class.getName() + ".THEME_RESOLVER";
    /** @deprecated */
    @Deprecated
    public static final String THEME_SOURCE_ATTRIBUTE = DispatcherServlet.class.getName() + ".THEME_SOURCE";
    public static final String INPUT_FLASH_MAP_ATTRIBUTE = DispatcherServlet.class.getName() + ".INPUT_FLASH_MAP";
    public static final String OUTPUT_FLASH_MAP_ATTRIBUTE = DispatcherServlet.class.getName() + ".OUTPUT_FLASH_MAP";
    public static final String FLASH_MAP_MANAGER_ATTRIBUTE = DispatcherServlet.class.getName() + ".FLASH_MAP_MANAGER";
    public static final String EXCEPTION_ATTRIBUTE = DispatcherServlet.class.getName() + ".EXCEPTION";
    public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
    private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";
    private static final String DEFAULT_STRATEGIES_PREFIX = "org.springframework.web.servlet";
    protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");
    @Nullable
    private static Properties defaultStrategies;
    private boolean detectAllHandlerMappings = true;
    private boolean detectAllHandlerAdapters = true;
    private boolean detectAllHandlerExceptionResolvers = true;
    private boolean detectAllViewResolvers = true;
    private boolean throwExceptionIfNoHandlerFound = true;
    private boolean cleanupAfterInclude = true;
    @Nullable
    private MultipartResolver multipartResolver;
    @Nullable
    private LocaleResolver localeResolver;
    /** @deprecated */
    @Deprecated
    @Nullable
    private ThemeResolver themeResolver;
    @Nullable
    private List<HandlerMapping> handlerMappings;
    @Nullable
    private List<HandlerAdapter> handlerAdapters;
    @Nullable
    private List<HandlerExceptionResolver> handlerExceptionResolvers;
    @Nullable
    private RequestToViewNameTranslator viewNameTranslator;
    @Nullable
    private FlashMapManager flashMapManager;
    @Nullable
    private List<ViewResolver> viewResolvers;
    private boolean parseRequestPath;

    public DispatcherServlet() {
        this.setDispatchOptionsRequest(true);
    }

    public DispatcherServlet(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
        this.setDispatchOptionsRequest(true);
    }

    public void setDetectAllHandlerMappings(boolean detectAllHandlerMappings) {
        this.detectAllHandlerMappings = detectAllHandlerMappings;
    }

    public void setDetectAllHandlerAdapters(boolean detectAllHandlerAdapters) {
        this.detectAllHandlerAdapters = detectAllHandlerAdapters;
    }

    public void setDetectAllHandlerExceptionResolvers(boolean detectAllHandlerExceptionResolvers) {
        this.detectAllHandlerExceptionResolvers = detectAllHandlerExceptionResolvers;
    }

    public void setDetectAllViewResolvers(boolean detectAllViewResolvers) {
        this.detectAllViewResolvers = detectAllViewResolvers;
    }

    /** @deprecated */
    @Deprecated(
        since = "6.1",
        forRemoval = true
    )
    public void setThrowExceptionIfNoHandlerFound(boolean throwExceptionIfNoHandlerFound) {
        this.throwExceptionIfNoHandlerFound = throwExceptionIfNoHandlerFound;
    }

    public void setCleanupAfterInclude(boolean cleanupAfterInclude) {
        this.cleanupAfterInclude = cleanupAfterInclude;
    }

    protected void onRefresh(ApplicationContext context) {
        this.initStrategies(context);
    }

    protected void initStrategies(ApplicationContext context) {
        this.initMultipartResolver(context);
        this.initLocaleResolver(context);
        this.initThemeResolver(context);
        this.initHandlerMappings(context);
        this.initHandlerAdapters(context);
        this.initHandlerExceptionResolvers(context);
        this.initRequestToViewNameTranslator(context);
        this.initViewResolvers(context);
        this.initFlashMapManager(context);
    }

    private void initMultipartResolver(ApplicationContext context) {
        try {
            this.multipartResolver = (MultipartResolver)context.getBean("multipartResolver", MultipartResolver.class);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Detected " + this.multipartResolver);
            } else if (this.logger.isDebugEnabled()) {
                this.logger.debug("Detected " + this.multipartResolver.getClass().getSimpleName());
            }
        } catch (NoSuchBeanDefinitionException var3) {
            this.multipartResolver = null;
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("No MultipartResolver 'multipartResolver' declared");
            }
        }

    }

    private void initLocaleResolver(ApplicationContext context) {
        try {
            this.localeResolver = (LocaleResolver)context.getBean("localeResolver", LocaleResolver.class);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Detected " + this.localeResolver);
            } else if (this.logger.isDebugEnabled()) {
                this.logger.debug("Detected " + this.localeResolver.getClass().getSimpleName());
            }
        } catch (NoSuchBeanDefinitionException var3) {
            this.localeResolver = (LocaleResolver)this.getDefaultStrategy(context, LocaleResolver.class);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("No LocaleResolver 'localeResolver': using default [" + this.localeResolver.getClass().getSimpleName() + "]");
            }
        }

    }

    /** @deprecated */
    @Deprecated
    private void initThemeResolver(ApplicationContext context) {
        try {
            this.themeResolver = (ThemeResolver)context.getBean("themeResolver", ThemeResolver.class);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Detected " + this.themeResolver);
            } else if (this.logger.isDebugEnabled()) {
                this.logger.debug("Detected " + this.themeResolver.getClass().getSimpleName());
            }
        } catch (NoSuchBeanDefinitionException var3) {
            this.themeResolver = (ThemeResolver)this.getDefaultStrategy(context, ThemeResolver.class);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("No ThemeResolver 'themeResolver': using default [" + this.themeResolver.getClass().getSimpleName() + "]");
            }
        }

    }

    private void initHandlerMappings(ApplicationContext context) {
        this.handlerMappings = null;
        if (this.detectAllHandlerMappings) {
            Map<String, HandlerMapping> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerMapping.class, true, false);
            if (!matchingBeans.isEmpty()) {
                this.handlerMappings = new ArrayList(matchingBeans.values());
                AnnotationAwareOrderComparator.sort(this.handlerMappings);
            }
        } else {
            try {
                HandlerMapping hm = (HandlerMapping)context.getBean("handlerMapping", HandlerMapping.class);
                this.handlerMappings = Collections.singletonList(hm);
            } catch (NoSuchBeanDefinitionException var4) {
            }
        }

        if (this.handlerMappings == null) {
            this.handlerMappings = this.getDefaultStrategies(context, HandlerMapping.class);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("No HandlerMappings declared for servlet '" + this.getServletName() + "': using default strategies from DispatcherServlet.properties");
            }
        }

        Iterator var6 = this.handlerMappings.iterator();

        while(var6.hasNext()) {
            HandlerMapping mapping = (HandlerMapping)var6.next();
            if (mapping.usesPathPatterns()) {
                this.parseRequestPath = true;
                break;
            }
        }

    }

    private void initHandlerAdapters(ApplicationContext context) {
        this.handlerAdapters = null;
        if (this.detectAllHandlerAdapters) {
            Map<String, HandlerAdapter> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerAdapter.class, true, false);
            if (!matchingBeans.isEmpty()) {
                this.handlerAdapters = new ArrayList(matchingBeans.values());
                AnnotationAwareOrderComparator.sort(this.handlerAdapters);
            }
        } else {
            try {
                HandlerAdapter ha = (HandlerAdapter)context.getBean("handlerAdapter", HandlerAdapter.class);
                this.handlerAdapters = Collections.singletonList(ha);
            } catch (NoSuchBeanDefinitionException var3) {
            }
        }

        if (this.handlerAdapters == null) {
            this.handlerAdapters = this.getDefaultStrategies(context, HandlerAdapter.class);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("No HandlerAdapters declared for servlet '" + this.getServletName() + "': using default strategies from DispatcherServlet.properties");
            }
        }

    }

    private void initHandlerExceptionResolvers(ApplicationContext context) {
        this.handlerExceptionResolvers = null;
        if (this.detectAllHandlerExceptionResolvers) {
            Map<String, HandlerExceptionResolver> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerExceptionResolver.class, true, false);
            if (!matchingBeans.isEmpty()) {
                this.handlerExceptionResolvers = new ArrayList(matchingBeans.values());
                AnnotationAwareOrderComparator.sort(this.handlerExceptionResolvers);
            }
        } else {
            try {
                HandlerExceptionResolver her = (HandlerExceptionResolver)context.getBean("handlerExceptionResolver", HandlerExceptionResolver.class);
                this.handlerExceptionResolvers = Collections.singletonList(her);
            } catch (NoSuchBeanDefinitionException var3) {
            }
        }

        if (this.handlerExceptionResolvers == null) {
            this.handlerExceptionResolvers = this.getDefaultStrategies(context, HandlerExceptionResolver.class);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("No HandlerExceptionResolvers declared in servlet '" + this.getServletName() + "': using default strategies from DispatcherServlet.properties");
            }
        }

    }

    private void initRequestToViewNameTranslator(ApplicationContext context) {
        try {
            this.viewNameTranslator = (RequestToViewNameTranslator)context.getBean("viewNameTranslator", RequestToViewNameTranslator.class);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Detected " + this.viewNameTranslator.getClass().getSimpleName());
            } else if (this.logger.isDebugEnabled()) {
                this.logger.debug("Detected " + this.viewNameTranslator);
            }
        } catch (NoSuchBeanDefinitionException var3) {
            this.viewNameTranslator = (RequestToViewNameTranslator)this.getDefaultStrategy(context, RequestToViewNameTranslator.class);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("No RequestToViewNameTranslator 'viewNameTranslator': using default [" + this.viewNameTranslator.getClass().getSimpleName() + "]");
            }
        }

    }

    private void initViewResolvers(ApplicationContext context) {
        this.viewResolvers = null;
        if (this.detectAllViewResolvers) {
            Map<String, ViewResolver> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, ViewResolver.class, true, false);
            if (!matchingBeans.isEmpty()) {
                this.viewResolvers = new ArrayList(matchingBeans.values());
                AnnotationAwareOrderComparator.sort(this.viewResolvers);
            }
        } else {
            try {
                ViewResolver vr = (ViewResolver)context.getBean("viewResolver", ViewResolver.class);
                this.viewResolvers = Collections.singletonList(vr);
            } catch (NoSuchBeanDefinitionException var3) {
            }
        }

        if (this.viewResolvers == null) {
            this.viewResolvers = this.getDefaultStrategies(context, ViewResolver.class);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("No ViewResolvers declared for servlet '" + this.getServletName() + "': using default strategies from DispatcherServlet.properties");
            }
        }

    }

    private void initFlashMapManager(ApplicationContext context) {
        try {
            this.flashMapManager = (FlashMapManager)context.getBean("flashMapManager", FlashMapManager.class);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Detected " + this.flashMapManager.getClass().getSimpleName());
            } else if (this.logger.isDebugEnabled()) {
                this.logger.debug("Detected " + this.flashMapManager);
            }
        } catch (NoSuchBeanDefinitionException var3) {
            this.flashMapManager = (FlashMapManager)this.getDefaultStrategy(context, FlashMapManager.class);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("No FlashMapManager 'flashMapManager': using default [" + this.flashMapManager.getClass().getSimpleName() + "]");
            }
        }

    }

    /** @deprecated */
    @Deprecated
    @Nullable
    public final ThemeSource getThemeSource() {
        WebApplicationContext var2 = this.getWebApplicationContext();
        ThemeSource var10000;
        if (var2 instanceof ThemeSource themeSource) {
            var10000 = themeSource;
        } else {
            var10000 = null;
        }

        return var10000;
    }

    @Nullable
    public final MultipartResolver getMultipartResolver() {
        return this.multipartResolver;
    }

    @Nullable
    public final List<HandlerMapping> getHandlerMappings() {
        return this.handlerMappings != null ? Collections.unmodifiableList(this.handlerMappings) : null;
    }

    protected <T> T getDefaultStrategy(ApplicationContext context, Class<T> strategyInterface) {
        List<T> strategies = this.getDefaultStrategies(context, strategyInterface);
        if (strategies.size() != 1) {
            throw new BeanInitializationException("DispatcherServlet needs exactly 1 strategy for interface [" + strategyInterface.getName() + "]");
        } else {
            return strategies.get(0);
        }
    }

    protected <T> List<T> getDefaultStrategies(ApplicationContext context, Class<T> strategyInterface) {
        if (defaultStrategies == null) {
            try {
                ClassPathResource resource = new ClassPathResource("DispatcherServlet.properties", DispatcherServlet.class);
                defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
            } catch (IOException var15) {
                IOException ex = var15;
                throw new IllegalStateException("Could not load 'DispatcherServlet.properties': " + ex.getMessage());
            }
        }

        String key = strategyInterface.getName();
        String value = defaultStrategies.getProperty(key);
        if (value == null) {
            return Collections.emptyList();
        } else {
            String[] classNames = StringUtils.commaDelimitedListToStringArray(value);
            List<T> strategies = new ArrayList(classNames.length);
            String[] var7 = classNames;
            int var8 = classNames.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                String className = var7[var9];

                try {
                    Class<?> clazz = ClassUtils.forName(className, DispatcherServlet.class.getClassLoader());
                    Object strategy = this.createDefaultStrategy(context, clazz);
                    strategies.add(strategy);
                } catch (ClassNotFoundException var13) {
                    throw new BeanInitializationException("Could not find DispatcherServlet's default strategy class [" + className + "] for interface [" + key + "]", var13);
                } catch (LinkageError var14) {
                    LinkageError err = var14;
                    throw new BeanInitializationException("Unresolvable class definition for DispatcherServlet's default strategy class [" + className + "] for interface [" + key + "]", err);
                }
            }

            return strategies;
        }
    }

    protected Object createDefaultStrategy(ApplicationContext context, Class<?> clazz) {
        return context.getAutowireCapableBeanFactory().createBean(clazz);
    }

    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.logRequest(request);
        Map<String, Object> attributesSnapshot = null;
        if (WebUtils.isIncludeRequest(request)) {
            attributesSnapshot = new HashMap();
            Enumeration<?> attrNames = request.getAttributeNames();

            label116:
            while(true) {
                String attrName;
                do {
                    if (!attrNames.hasMoreElements()) {
                        break label116;
                    }

                    attrName = (String)attrNames.nextElement();
                } while(!this.cleanupAfterInclude && !attrName.startsWith("org.springframework.web.servlet"));

                attributesSnapshot.put(attrName, request.getAttribute(attrName));
            }
        }

        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.getWebApplicationContext());
        request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
        request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver);
        request.setAttribute(THEME_SOURCE_ATTRIBUTE, this.getThemeSource());
        if (this.flashMapManager != null) {
            FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(request, response);
            if (inputFlashMap != null) {
                request.setAttribute(INPUT_FLASH_MAP_ATTRIBUTE, Collections.unmodifiableMap(inputFlashMap));
            }

            request.setAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE, new FlashMap());
            request.setAttribute(FLASH_MAP_MANAGER_ATTRIBUTE, this.flashMapManager);
        }

        RequestPath previousRequestPath = null;
        if (this.parseRequestPath) {
            previousRequestPath = (RequestPath)request.getAttribute(ServletRequestPathUtils.PATH_ATTRIBUTE);
            ServletRequestPathUtils.parseAndCache(request);
        }

        try {
            this.doDispatch(request, response);
        } finally {
            if (!WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted() && attributesSnapshot != null) {
                this.restoreAttributesAfterInclude(request, attributesSnapshot);
            }

            if (this.parseRequestPath) {
                ServletRequestPathUtils.setParsedRequestPath(previousRequestPath, request);
            }

        }

    }

    private void logRequest(HttpServletRequest request) {
        LogFormatUtils.traceDebug(this.logger, (traceOn) -> {
            String contentType = request.getContentType();
            String params;
            if (StringUtils.startsWithIgnoreCase(contentType, "multipart/")) {
                params = "multipart";
            } else if (this.isEnableLoggingRequestDetails()) {
                params = (String)request.getParameterMap().entrySet().stream().map((entry) -> {
                    String var10000 = (String)entry.getKey();
                    return var10000 + ":" + Arrays.toString((Object[])entry.getValue());
                }).collect(Collectors.joining(", "));
            } else {
                params = !StringUtils.startsWithIgnoreCase(contentType, "application/x-www-form-urlencoded") && request.getParameterMap().isEmpty() ? "" : "masked";
            }

            String queryString = request.getQueryString();
            String queryClause = StringUtils.hasLength(queryString) ? "?" + queryString : "";
            String dispatchType = !DispatcherType.REQUEST.equals(request.getDispatcherType()) ? "\"" + request.getDispatcherType() + "\" dispatch for " : "";
            String message = dispatchType + request.getMethod() + " \"" + getRequestUri(request) + queryClause + "\", parameters={" + params + "}";
            if (traceOn) {
                List<String> values = Collections.list(request.getHeaderNames());
                String headers;
                if (this.isEnableLoggingRequestDetails()) {
                    headers = (String)values.stream().map((name) -> {
                        return name + ":" + Collections.list(request.getHeaders(name));
                    }).collect(Collectors.joining(", "));
                } else {
                    headers = !values.isEmpty() ? "masked" : "";
                }

                return message + ", headers={" + headers + "} in DispatcherServlet '" + this.getServletName() + "'";
            } else {
                return message;
            }
        });
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequest processedRequest = request;
        HandlerExecutionChain mappedHandler = null;
        boolean multipartRequestParsed = false;
        WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

        try {
            ModelAndView mv = null;
            Exception dispatchException = null;

            try {
                processedRequest = this.checkMultipart(request);
                multipartRequestParsed = processedRequest != request;
                mappedHandler = this.getHandler(processedRequest);
                if (mappedHandler == null) {
                    this.noHandlerFound(processedRequest, response);
                    return;
                }

                HandlerAdapter ha = this.getHandlerAdapter(mappedHandler.getHandler());
                String method = request.getMethod();
                boolean isGet = HttpMethod.GET.matches(method);
                if (isGet || HttpMethod.HEAD.matches(method)) {
                    long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
                    if ((new ServletWebRequest(request, response)).checkNotModified(lastModified) && isGet) {
                        return;
                    }
                }

                if (!mappedHandler.applyPreHandle(processedRequest, response)) {
                    return;
                }

                mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
                if (asyncManager.isConcurrentHandlingStarted()) {
                    return;
                }

                this.applyDefaultViewName(processedRequest, mv);
                mappedHandler.applyPostHandle(processedRequest, response, mv);
            } catch (Exception var20) {
                Exception ex = var20;
                dispatchException = ex;
            } catch (Throwable var21) {
                Throwable err = var21;
                dispatchException = new ServletException("Handler dispatch failed: " + err, err);
            }

            this.processDispatchResult(processedRequest, response, mappedHandler, mv, (Exception)dispatchException);
        } catch (Exception var22) {
            Exception ex = var22;
            triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
        } catch (Throwable var23) {
            Throwable err = var23;
            triggerAfterCompletion(processedRequest, response, mappedHandler, new ServletException("Handler processing failed: " + err, err));
        } finally {
            if (asyncManager.isConcurrentHandlingStarted()) {
                if (mappedHandler != null) {
                    mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
                }
            } else if (multipartRequestParsed) {
                this.cleanupMultipart(processedRequest);
            }

        }

    }

    private void applyDefaultViewName(HttpServletRequest request, @Nullable ModelAndView mv) throws Exception {
        if (mv != null && !mv.hasView()) {
            String defaultViewName = this.getDefaultViewName(request);
            if (defaultViewName != null) {
                mv.setViewName(defaultViewName);
            }
        }

    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, @Nullable HandlerExecutionChain mappedHandler, @Nullable ModelAndView mv, @Nullable Exception exception) throws Exception {
        boolean errorView = false;
        if (exception != null) {
            if (exception instanceof ModelAndViewDefiningException) {
                ModelAndViewDefiningException mavDefiningException = (ModelAndViewDefiningException)exception;
                this.logger.debug("ModelAndViewDefiningException encountered", exception);
                mv = mavDefiningException.getModelAndView();
            } else {
                Object handler = mappedHandler != null ? mappedHandler.getHandler() : null;
                mv = this.processHandlerException(request, response, handler, exception);
                errorView = mv != null;
            }
        }

        if (mv != null && !mv.wasCleared()) {
            this.render(mv, request, response);
            if (errorView) {
                WebUtils.clearErrorRequestAttributes(request);
            }
        } else if (this.logger.isTraceEnabled()) {
            this.logger.trace("No view rendering, null ModelAndView returned.");
        }

        if (!WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
            if (mappedHandler != null) {
                mappedHandler.triggerAfterCompletion(request, response, (Exception)null);
            }

        }
    }

    protected LocaleContext buildLocaleContext(final HttpServletRequest request) {
        LocaleResolver lr = this.localeResolver;
        if (lr instanceof LocaleContextResolver localeContextResolver) {
            return localeContextResolver.resolveLocaleContext(request);
        } else {
            return () -> {
                return lr != null ? lr.resolveLocale(request) : request.getLocale();
            };
        }
    }

    protected HttpServletRequest checkMultipart(HttpServletRequest request) throws MultipartException {
        if (this.multipartResolver != null && this.multipartResolver.isMultipart(request)) {
            if (WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class) != null) {
                if (DispatcherType.REQUEST.equals(request.getDispatcherType())) {
                    this.logger.trace("Request already resolved to MultipartHttpServletRequest, e.g. by MultipartFilter");
                }
            } else if (hasMultipartException(request)) {
                this.logger.debug("Multipart resolution previously failed for current request - skipping re-resolution for undisturbed error rendering");
            } else {
                MultipartException ex;
                try {
                    return this.multipartResolver.resolveMultipart(request);
                } catch (MultipartException var3) {
                    ex = var3;
                    if (request.getAttribute("jakarta.servlet.error.exception") == null) {
                        throw ex;
                    }
                }

                this.logger.debug("Multipart resolution failed for error dispatch", ex);
            }
        }

        return request;
    }

    private static boolean hasMultipartException(HttpServletRequest request) {
        for(Throwable error = (Throwable)request.getAttribute("jakarta.servlet.error.exception"); error != null; error = error.getCause()) {
            if (error instanceof MultipartException) {
                return true;
            }
        }

        return false;
    }

    protected void cleanupMultipart(HttpServletRequest request) {
        if (this.multipartResolver != null) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
            if (multipartRequest != null) {
                this.multipartResolver.cleanupMultipart(multipartRequest);
            }
        }

    }

    @Nullable
    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        if (this.handlerMappings != null) {
            Iterator var2 = this.handlerMappings.iterator();

            while(var2.hasNext()) {
                HandlerMapping mapping = (HandlerMapping)var2.next();
                HandlerExecutionChain handler = mapping.getHandler(request);
                if (handler != null) {
                    return handler;
                }
            }
        }

        return null;
    }

    protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (pageNotFoundLogger.isWarnEnabled()) {
            Log var10000 = pageNotFoundLogger;
            String var10001 = request.getMethod();
            var10000.warn("No mapping for " + var10001 + " " + getRequestUri(request));
        }

        if (this.throwExceptionIfNoHandlerFound) {
            throw new NoHandlerFoundException(request.getMethod(), getRequestUri(request), (new ServletServerHttpRequest(request)).getHeaders());
        } else {
            response.sendError(404);
        }
    }

    protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        if (this.handlerAdapters != null) {
            Iterator var2 = this.handlerAdapters.iterator();

            while(var2.hasNext()) {
                HandlerAdapter adapter = (HandlerAdapter)var2.next();
                if (adapter.supports(handler)) {
                    return adapter;
                }
            }
        }

        throw new ServletException("No adapter for handler [" + handler + "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
    }

    @Nullable
    protected ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) throws Exception {
        request.removeAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);

        try {
            response.resetBuffer();
        } catch (IllegalStateException var8) {
        }

        ModelAndView exMv = null;
        if (this.handlerExceptionResolvers != null) {
            Iterator var6 = this.handlerExceptionResolvers.iterator();

            while(var6.hasNext()) {
                HandlerExceptionResolver resolver = (HandlerExceptionResolver)var6.next();
                exMv = resolver.resolveException(request, response, handler, ex);
                if (exMv != null) {
                    break;
                }
            }
        }

        if (exMv != null) {
            if (exMv.isEmpty()) {
                request.setAttribute(EXCEPTION_ATTRIBUTE, ex);
                return null;
            } else {
                if (!exMv.hasView()) {
                    String defaultViewName = this.getDefaultViewName(request);
                    if (defaultViewName != null) {
                        exMv.setViewName(defaultViewName);
                    }
                }

                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("Using resolved error view: " + exMv, ex);
                } else if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Using resolved error view: " + exMv);
                }

                WebUtils.exposeErrorRequestAttributes(request, ex, this.getServletName());
                return exMv;
            }
        } else {
            throw ex;
        }
    }

    protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Locale locale = this.localeResolver != null ? this.localeResolver.resolveLocale(request) : request.getLocale();
        response.setLocale(locale);
        String viewName = mv.getViewName();
        View view;
        if (viewName != null) {
            view = this.resolveViewName(viewName, mv.getModelInternal(), locale, request);
            if (view == null) {
                String var10002 = mv.getViewName();
                throw new ServletException("Could not resolve view with name '" + var10002 + "' in servlet with name '" + this.getServletName() + "'");
            }
        } else {
            view = mv.getView();
            if (view == null) {
                throw new ServletException("ModelAndView [" + mv + "] neither contains a view name nor a View object in servlet with name '" + this.getServletName() + "'");
            }
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Rendering view [" + view + "] ");
        }

        try {
            if (mv.getStatus() != null) {
                request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, mv.getStatus());
                response.setStatus(mv.getStatus().value());
            }

            view.render(mv.getModelInternal(), request, response);
        } catch (Exception var8) {
            Exception ex = var8;
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Error rendering view [" + view + "]", ex);
            }

            throw ex;
        }
    }

    @Nullable
    protected String getDefaultViewName(HttpServletRequest request) throws Exception {
        return this.viewNameTranslator != null ? this.viewNameTranslator.getViewName(request) : null;
    }

    @Nullable
    protected View resolveViewName(String viewName, @Nullable Map<String, Object> model, Locale locale, HttpServletRequest request) throws Exception {
        if (this.viewResolvers != null) {
            Iterator var5 = this.viewResolvers.iterator();

            while(var5.hasNext()) {
                ViewResolver viewResolver = (ViewResolver)var5.next();
                View view = viewResolver.resolveViewName(viewName, locale);
                if (view != null) {
                    return view;
                }
            }
        }

        return null;
    }

    private static void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, @Nullable HandlerExecutionChain mappedHandler, Exception ex) throws Exception {
        if (mappedHandler != null) {
            mappedHandler.triggerAfterCompletion(request, response, ex);
        }

        throw ex;
    }

    private void restoreAttributesAfterInclude(HttpServletRequest request, Map<?, ?> attributesSnapshot) {
        Set<String> attrsToCheck = new HashSet();
        Enumeration<?> attrNames = request.getAttributeNames();

        while(true) {
            String attrName;
            do {
                if (!attrNames.hasMoreElements()) {
                    attrsToCheck.addAll(attributesSnapshot.keySet());
                    Iterator var8 = attrsToCheck.iterator();

                    while(var8.hasNext()) {
                        String attrName = (String)var8.next();
                        Object attrValue = attributesSnapshot.get(attrName);
                        if (attrValue == null) {
                            request.removeAttribute(attrName);
                        } else if (attrValue != request.getAttribute(attrName)) {
                            request.setAttribute(attrName, attrValue);
                        }
                    }

                    return;
                }

                attrName = (String)attrNames.nextElement();
            } while(!this.cleanupAfterInclude && !attrName.startsWith("org.springframework.web.servlet"));

            attrsToCheck.add(attrName);
        }
    }

    private static String getRequestUri(HttpServletRequest request) {
        String uri = (String)request.getAttribute("jakarta.servlet.include.request_uri");
        if (uri == null) {
            uri = request.getRequestURI();
        }

        return uri;
    }
}
