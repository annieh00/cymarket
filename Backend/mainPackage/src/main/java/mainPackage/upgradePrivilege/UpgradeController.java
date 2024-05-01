package mainPackage.upgradePrivilege;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mainPackage.usersPackage.GeneralUser;
import mainPackage.usersPackage.GeneralUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UpgradeController {
    @Autowired
    private UpgradeQueueRepository upgradeQueueRepository;

    @Autowired
    private GeneralUserRepository generalUserRepository;

    @PostMapping("/requestUpgrade")
    public String upgradeUser(@RequestBody UpgradeQueue uq){
        GeneralUser u = generalUserRepository.findGeneralUserByUserName(uq.getUserName());
        if(u != null){
           upgradeQueueRepository.save(uq);
           return "{\"serverResponse\" : true}";
        }else{
            return "{\"serverResponse\" : false}";
        }
    }

    @PostMapping("/approveUpgrade/{userName}")
    public String approveUpgrade(@PathVariable(name = "userName") String userName){
        UpgradeQueue uq = upgradeQueueRepository.findUpgradeQueueByUserName(userName);
        if(uq != null){
            GeneralUser u = generalUserRepository.findGeneralUserByUserName(uq.getUserName());
            if(u != null){
                u.setUserType(1);
                generalUserRepository.save(u);
                upgradeQueueRepository.delete(uq);
                return "{\"serverResponse\" : true}";
            }
            return "{\"serverResponse\" : false}";
        }else{
            return "{\"serverResponse\" : false}";
        }
    }

    @GetMapping("/getAllUpgradeRequests")
    public String getAllUpgrades(){
        List<UpgradeQueue> list = upgradeQueueRepository.findAll();
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(list);
        return "{ \"upgradeList\" :" +json + "}";
    }



}
