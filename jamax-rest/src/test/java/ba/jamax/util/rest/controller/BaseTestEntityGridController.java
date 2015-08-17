package ba.jamax.util.rest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ba.jamax.util.rest.model.BaseTestEntity;
import ba.jamax.util.rest.service.BaseTestEntityService;

@Controller
@RequestMapping("/baseTestEntity")
public class BaseTestEntityGridController extends AbstractRestControllerImpl<BaseTestEntity> {
	
	@Override
	protected Class<BaseTestEntity> getPersistentClass() {
		return BaseTestEntity.class;
	}
    @Override
	public BaseTestEntityService getService() {
        return (BaseTestEntityService) super.getService();
    }
    
    @Autowired
    public void setNetworkConfigService(final BaseTestEntityService BaseTestEntityService) {
        super.setService(BaseTestEntityService);
    }        
}