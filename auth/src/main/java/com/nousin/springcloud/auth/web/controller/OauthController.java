package com.nousin.springcloud.auth.web.controller;

import com.nousin.springcloud.common.dto.ResultDto;
import com.nousin.springcloud.common.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 重写令牌申请接口，并自定义返回格式
 *
 * @author tangwc
 * @since 2019/12/17
 */
@RestController
@RequestMapping("/oauth")
public class OauthController {
	@Autowired
	private TokenEndpoint tokenEndpoint;

	@GetMapping("/token")
	public ResultDto getAccessToken(Principal principal, @RequestParam Map<String, String> parameters) {
		try {
			return customToken(tokenEndpoint.getAccessToken(principal, parameters).getBody());
		} catch (HttpRequestMethodNotSupportedException e) {
			return ResultUtil.fail(e.getMessage());
		} catch (OAuth2Exception e) {
			return ResultUtil.authFailed(e.getOAuth2ErrorCode(), e.getMessage());
		} catch (Exception e) {
			return ResultUtil.authError();
		}
	}

	@PostMapping("/token")
	public ResultDto postAccessToken(Principal principal, @RequestParam Map<String, String> parameters, HttpServletRequest request) {
		try {
			return customToken(tokenEndpoint.postAccessToken(principal, parameters).getBody());
		} catch (HttpRequestMethodNotSupportedException e) {
			return ResultUtil.fail(e.getMessage());
		} catch (OAuth2Exception e) {
			return ResultUtil.authFailed(e.getOAuth2ErrorCode(), e.getMessage());
		} catch (Exception e) {
			return ResultUtil.authError();
		}
	}

	//自定义返回格式
	private ResultDto customToken(OAuth2AccessToken accessToken) {
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("accessToken", token.getValue());
		// data.put("additionalInformation", token.getAdditionalInformation());
		if (token.getRefreshToken() != null) {
			data.put("refreshToken", token.getRefreshToken().getValue());
		}
		return ResultUtil.success(data);
	}
}
