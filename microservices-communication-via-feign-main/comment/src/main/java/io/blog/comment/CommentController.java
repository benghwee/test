/**
 * Created by IntelliJ IDEA.
 * User: Cangue.Jamba
 * Project name: microservices-communication-via-feign
 */
package io.blog.comment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;
import org.springframework.http.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/comments")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentDto registerComment(@RequestBody CommentRegisterRequest commentRegisterRequest) {
        log.info("new Comment registration {}", commentRegisterRequest);
        return commentService.registerComment(commentRegisterRequest);
    }

    @GetMapping(path = "{articleId}")
    public List<CommentDto> getComment(@PathVariable(value = "articleId") Long articleId) throws Exception{
        log.info("get Comment by Article id {}", articleId);
        if(true) {
        	throw new Exception("");
        }
        return commentService.getComment(articleId);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody ErrorInfo
    handleBadRequest(HttpServletRequest req, Exception ex) {
    	System.out.println("In handleBadRequest");
        return new ErrorInfo(req.getRequestURL().toString(), ex);
    } 

}
