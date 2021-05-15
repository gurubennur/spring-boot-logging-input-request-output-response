package example.springboot.jpa.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class HttpLoggingFilter extends OncePerRequestFilter {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private AtomicLong requestId = new AtomicLong(1);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final long id = requestId.incrementAndGet();
		final RequestWrapper requestWrapper = new RequestWrapper(id, request);
		final ResponseWrapper responseWrapper = new ResponseWrapper(id, response);
		filterChain.doFilter(requestWrapper, responseWrapper);
		try {
			final HttpInfo httpInfo = getHttpInfo(requestWrapper, responseWrapper);
			ObjectMapper objectMapper = new ObjectMapper();
			String indented = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(httpInfo);
			log.info("Request and response: id={} info={}", id, indented);
		} catch (UnsupportedEncodingException ex) {
			log.error("Error in logging http request and response id={} error={}", id, ex);
		}

	}

	public HttpInfo getHttpInfo(RequestWrapper requestWrapper, ResponseWrapper responseWrapper)
			throws UnsupportedEncodingException {
		return HttpInfo.builder().url(getUrl(requestWrapper)).requestHeaders(getRequestHeaders(requestWrapper))
				.requestBody(getRequestBody(requestWrapper)).responseHeaders(getResponseHeaders(responseWrapper))
				.responseBody(getResponseBody(responseWrapper)).build();
	}

	public Map<String, String> getRequestHeaders(RequestWrapper requestWrapper) {
		Map<String, String> headers = new HashMap<>();
		Collections.list(requestWrapper.getHeaderNames())
				.forEach(key -> headers.put(key, requestWrapper.getHeader(key)));
		return headers;
	}

	public Map<String, String> getResponseHeaders(ResponseWrapper responseWrapper) {
		Map<String, String> headers = new HashMap<>();
		responseWrapper.getHeaderNames().forEach(key -> headers.put(key, responseWrapper.getHeader(key)));
		return headers;
	}

	public String getUrl(RequestWrapper requestWrapper) {
		return requestWrapper.getRequestURL() + "?" + requestWrapper.getQueryString();
	}

	public String getRequestBody(RequestWrapper requestWrapper) throws UnsupportedEncodingException {
		String charEncoding = requestWrapper.getCharacterEncoding() != null ? requestWrapper.getCharacterEncoding()
				: "UTF-8";
		return new String(requestWrapper.toByteArray(), charEncoding);
	}

	public String getResponseBody(ResponseWrapper responseWrapper) throws UnsupportedEncodingException {
		return new String(responseWrapper.toByteArray(), responseWrapper.getCharacterEncoding());
	}

}