package com.tustosc.setsail.Controller;

import com.tustosc.setsail.Entiy.Tutorial;
import com.tustosc.setsail.Enums.RedisPrefix;
import com.tustosc.setsail.Service.TutorialService;
import com.tustosc.setsail.Utils.IpUtils;
import com.tustosc.setsail.Utils.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tutorial")
public class TutorialController {

    @Autowired
    private TutorialService tutorialService;
    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/list")
    public List<Tutorial> getAllTutorials() {
        return tutorialService.getAllTutorials();
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateTutorial(@RequestBody Tutorial tutorial, HttpServletRequest request) {
        if(tutorialService.update(tutorial, IpUtils.getIpAddress(request))){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteTutorial(@RequestParam String id, HttpServletRequest request){
        if(redisUtils.set(RedisPrefix.DELETE_TUTORIAL, id, "", 3600L)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }



}
