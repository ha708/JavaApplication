package org.example.api;
import org.example.model.MxxModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/happy")

public class HappyController {
    private static final Logger log = LoggerFactory.getLogger(HappyController.class);

    @Autowired
    MxxModel model;

    @GetMapping(value = "/mxx")
    public MxxModel mxx() {
        return model;
    }

    @PostMapping(value = "/mxx")
    public MxxModel setMxx(@RequestBody MxxModel mxx) {
        model.setValue(mxx.getValue());
        log.info("set value:{}",model.getValue());
        return mxx;
    }

}
