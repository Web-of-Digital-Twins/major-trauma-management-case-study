/*
 * Copyright (c) 2024. Andrea Giulianelli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.wodt.physicaladapter;

import io.github.wodt.model.NameSurname;
import io.github.wodt.model.Qualification;
import it.wldt.adapter.physical.PhysicalAdapter;
import it.wldt.adapter.physical.PhysicalAssetDescription;
import it.wldt.adapter.physical.PhysicalAssetProperty;
import it.wldt.adapter.physical.event.PhysicalAssetActionWldtEvent;
import it.wldt.exception.EventBusException;
import it.wldt.exception.PhysicalAdapterException;

import java.util.List;
import java.util.logging.Logger;

/** Rescue Physical Adapter. */
public class RescuerPhysicalAdapter extends PhysicalAdapter {
    private static final String IDENTIFIER_PROPERTY_KEY = "identifier";
    private static final String NAME_PROPERTY_KEY = "name";
    private static final String QUALIFICATION_PROPERTY_KEY = "qualification";

    /**
     * Default constructor.
     */
    public RescuerPhysicalAdapter() {
        super("rescuer-physical-adapter");
    }

    @Override
    public void onIncomingPhysicalAction(final PhysicalAssetActionWldtEvent<?> physicalActionEvent) {}

    @Override
    public void onAdapterStart() {
        final PhysicalAssetDescription pad = new PhysicalAssetDescription();
        final PhysicalAssetProperty<String> identifierProperty = new PhysicalAssetProperty<>(
                IDENTIFIER_PROPERTY_KEY, "ABC123");
        final PhysicalAssetProperty<NameSurname> nameProperty = new PhysicalAssetProperty<>(
                NAME_PROPERTY_KEY, new NameSurname("John", "Smith"));
        final PhysicalAssetProperty<Qualification> qualificationProperty = new PhysicalAssetProperty<>(
                QUALIFICATION_PROPERTY_KEY, new Qualification("397897005", "Paramedic"));
        pad.getProperties().addAll(List.of(identifierProperty, nameProperty, qualificationProperty));

        try {
            this.notifyPhysicalAdapterBound(pad);
        } catch (PhysicalAdapterException | EventBusException e) {
            Logger.getLogger(MissionPhysicalAdapter.class.getName()).info(e.getMessage());
        }
    }

    @Override
    public void onAdapterStop() {

    }
}
