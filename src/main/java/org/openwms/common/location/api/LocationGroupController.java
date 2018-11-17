/*
 * Copyright 2018 Heiko Scherrer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.common.location.api;

import org.ameba.exception.NotFoundException;
import org.ameba.i18n.Translator;
import org.ameba.mapping.BeanMapper;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.openwms.common.CommonConstants;
import org.openwms.common.CommonMessageCodes;
import org.openwms.common.location.LocationGroup;
import org.openwms.common.location.LocationGroupService;
import org.openwms.common.location.LocationGroupState;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A LocationGroupController.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
@RestController(CommonConstants.API_LOCATIONGROUPS)
class LocationGroupController implements LocationGroupApi {

    private final LocationGroupService locationGroupService;
    private final Translator translator;
    private final BeanMapper mapper;
    private final ErrorCodeTransformers.GroupStateIn groupStateIn;
    private final ErrorCodeTransformers.GroupStateOut groupStateOut;

    LocationGroupController(LocationGroupService locationGroupService, Translator translator, BeanMapper mapper, ErrorCodeTransformers.GroupStateIn groupStateIn, ErrorCodeTransformers.GroupStateOut groupStateOut) {
        this.locationGroupService = locationGroupService;
        this.translator = translator;
        this.mapper = mapper;
        this.groupStateIn = groupStateIn;
        this.groupStateOut = groupStateOut;
    }


    @Override
    @PatchMapping(value = CommonConstants.API_LOCATIONGROUPS + "/{id}")
    public void save(@PathVariable String id, @RequestParam(name = "statein", required = false) LocationGroupState stateIn, @RequestParam(name = "stateout", required = false) LocationGroupState stateOut, HttpServletRequest req, HttpServletResponse res) {
        locationGroupService.changeGroupState(id, stateIn, stateOut);
        res.addHeader(HttpHeaders.LOCATION, getLocationForCreatedResource(req, id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PatchMapping(value = CommonConstants.API_LOCATIONGROUPS, params = {"name"})
    public void updateState(@RequestParam(name = "name") String locationGroupName, @RequestBody ErrorCodeVO errorCode, HttpServletRequest req, HttpServletResponse res) {
        locationGroupService.changeGroupStates(locationGroupName, groupStateIn.transform(errorCode.errorCode), groupStateOut.transform(errorCode.errorCode));
    }

    @Override
    @GetMapping(value = CommonConstants.API_LOCATIONGROUPS, params = {"name"})
    public LocationGroupVO getLocationGroup(@RequestParam("name") String name) {
        Optional<LocationGroup> opt = locationGroupService.findByName(name);
        LocationGroup locationGroup = opt.orElseThrow(() -> new NotFoundException(translator, CommonMessageCodes.LOCATION_GROUP_NOT_FOUND, new String[]{name}, name));
        LocationGroupVO result = mapper.map(locationGroup, LocationGroupVO.class);
        if (locationGroup.hasParent()) {
            result.add(linkTo(methodOn(LocationGroupController.class).getLocationGroup(locationGroup.getParent().getName())).withRel("_parent"));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationGroupVO> findAll() {
        List<LocationGroup> all = locationGroupService.findAll();
        return all == null ? Collections.emptyList() : mapper.map(all, LocationGroupVO.class);
    }

    @Override
    public String getLocationForCreatedResource(HttpServletRequest req, String objId) {
        StringBuffer url = req.getRequestURL();
        UriTemplate template = new UriTemplate(url.append("/{objId}/").toString());
        return template.expand(objId).toASCIIString();
    }
}
