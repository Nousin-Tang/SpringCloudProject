package com.nousin.springcloud.server.storage.framework.exception;

import com.nousin.springcloud.common.dto.ResultDto;
import com.nousin.springcloud.common.util.ResultUtil;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 拦截 @Validated 校验失败的信息
 *
 * // 请求参数需写在请求路径上
 * @ RequestMapping(value = "/param", method = RequestMethod.GET)
 * public ResultDto param(@Validated @NotBlank(message = "参数不能为空") String param) {
 * 	return ResultUtil.success(param);
 * }
 *
 * // 请求参数需写在请求路径上，并只校验按照 UserGroup.Insert.class 分组的字段
 * @ RequestMapping(value = "/get", method = RequestMethod.GET)
 * public ResultDto get(@Validated(UserGroup.Insert.class) UserDto requestDto) {
 * 	return ResultUtil.success(requestDto);
 * }
 *
 * // 只校验按照 UserGroup.Insert.class 分组的字段
 * @ RequestMapping(value = "/user", method = RequestMethod.POST)
 * public ResultDto user(@RequestBody @Validated(UserGroup.Insert.class) UserDto requestDto) {
 * 	return ResultUtil.success(requestDto);
 * }
 *
 * // 只校验按照 CustomerGroup.Search.class 分组的字段
 * @ RequestMapping(value = "/customer", method = RequestMethod.POST)
 * public ResultDto customer(@RequestBody @Validated(CustomerGroup.Search.class) UserDto requestDto) {
 * 	return ResultUtil.success(requestDto);
 * }
 *
 * // 不指定分组 则校验未指定分组的字段
 * @ RequestMapping(value = "/other", method = RequestMethod.POST)
 * public ResultDto other(@RequestBody @Validated UserDto requestDto) {
 * 	return ResultUtil.success(requestDto);
 * }
 *
 * // 继承 Default 分组的校验，会校验指定的字段与未指定分组的字段
 * @ RequestMapping(value="useDefault", method = RequestMethod.POST)
 * public ResultDto groupDefault(@RequestBody @Validated(UserGroup.Common.class) UserDto requestDto){
 * 	return ResultUtil.success(requestDto);
 * }
 *
 * @author Nousin
 * @since 2019/12/18
 */
@ControllerAdvice
@Order(1)
public class ValidationExceptionHandler {
    /**
     * 处理Get请求中 使用@Validated 验证路径中请求实体校验失败后抛出的异常，详情继续往下看代码
     *
     * @param e
     * @return 返回错误信息
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResultDto BindExceptionHandler(BindException e) {
        return ResultUtil.fail(e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";")));
    }

    /**
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResultDto constraintViolationExceptionHandler(ConstraintViolationException e) {
        return ResultUtil.fail(e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.joining("；")));
    }

    /**
     * 处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常。
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultDto methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        return ResultUtil.fail(e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("；")));
    }


    /**
     * 访问接口参数不全
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResultDto missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResultUtil.fail("请求参数" + e.getParameterName() + "不存在");
    }

    /**
     * 请求方法不支持
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResultDto httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String method = Optional.ofNullable(e.getSupportedHttpMethods()).orElseGet(HashSet::new).stream()
                .map(HttpMethod::name).collect(Collectors.joining("、"));
        return ResultUtil.fail(e.getMethod() + "请求方式不正确，仅支持" + method);
    }
}
