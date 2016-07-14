/*
 * Copyright (C) 2012 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */


@Portlet
@Application(name = "CongesAdministrationApplication")
@Bindings({
        @Binding(OrganizationService.class),
        @Binding(CongesService.class)
})
@Stylesheets({
        @Stylesheet(value = "/org/exoplatform/tracfin/portlet/congesAdministration/assets/congesAdmin.css", location = AssetLocation.APPLICATION)
})
@Scripts({
    @Script(id = "jquery", value = "js/jquery-1.8.3.min.js", location = AssetLocation.SERVER)
})
@Less(value = {"congesAdmin.less"})
@Assets("*")
package org.exoplatform.tracfin.portlet.congesAdministration;

import juzu.Application;
import juzu.asset.AssetLocation;
import juzu.plugin.asset.*;
import juzu.plugin.binding.Binding;
import juzu.plugin.binding.Bindings;
import juzu.plugin.less.Less;
import juzu.plugin.portlet.Portlet;

import org.exoplatform.services.CongesService;
import org.exoplatform.services.organization.OrganizationService;

