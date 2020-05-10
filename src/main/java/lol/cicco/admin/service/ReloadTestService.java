package lol.cicco.admin.service;

import lol.cicco.admin.common.annotation.ChangePropertyListener;
import org.springframework.stereotype.Service;

@Service
public class ReloadTestService {

    @ChangePropertyListener(property = "aaa.aaa")
    public void reloadListenerA(String val) {
        System.out.println("Change value : "+ val);
    }

}
