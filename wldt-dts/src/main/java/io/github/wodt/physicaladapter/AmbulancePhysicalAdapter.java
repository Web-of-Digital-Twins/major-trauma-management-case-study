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

import io.github.wodt.model.Position;
import it.wldt.adapter.physical.PhysicalAdapter;
import it.wldt.adapter.physical.PhysicalAssetAction;
import it.wldt.adapter.physical.PhysicalAssetDescription;
import it.wldt.adapter.physical.PhysicalAssetProperty;
import it.wldt.adapter.physical.event.PhysicalAssetActionWldtEvent;
import it.wldt.adapter.physical.event.PhysicalAssetPropertyWldtEvent;
import it.wldt.exception.EventBusException;
import it.wldt.exception.PhysicalAdapterException;

import java.util.List;
import java.util.logging.Logger;

/** Ambulance Physical Adapter. */
public class AmbulancePhysicalAdapter extends PhysicalAdapter {
    private static final String STATUS_PROPERTY_KEY = "status";
    private static final String POSITION_PROPERTY_KEY = "position";
    private static final String SET_DESTINATION_ACTION_KEY = "setDestination";

    private static final String STATUS_INACTIVE = "inactive";
    private static final String STATUS_ACTIVE = "active";

    /**
     * Default constructor.
     */
    public AmbulancePhysicalAdapter() {
        super("ambulance-physical-adapter");
    }

    @Override
    public void onIncomingPhysicalAction(final PhysicalAssetActionWldtEvent<?> physicalActionEvent) {
        if (physicalActionEvent != null && physicalActionEvent.getActionKey().equals(SET_DESTINATION_ACTION_KEY)) {
            try {
                //Create a new event to notify the variation of a Physical Property
                final PhysicalAssetPropertyWldtEvent<String> newStatus = new PhysicalAssetPropertyWldtEvent<>(
                        STATUS_PROPERTY_KEY,
                        STATUS_ACTIVE
                );
                publishPhysicalAssetPropertyWldtEvent(newStatus);

                final PhysicalAssetPropertyWldtEvent<Position> newPosition = new PhysicalAssetPropertyWldtEvent<>(
                        POSITION_PROPERTY_KEY,
                        new Position(40.7127281, -74.0060152)
                );
                publishPhysicalAssetPropertyWldtEvent(newPosition);

                Logger.getLogger(AmbulancePhysicalAdapter.class.getName()).info("ACTION called: Set Destination");
            } catch (EventBusException e) {
                Logger.getLogger(AmbulancePhysicalAdapter.class.getName()).info(e.getMessage());
            }
        }
    }

    @Override
    public void onAdapterStart() {
        final PhysicalAssetDescription pad = new PhysicalAssetDescription();
        final PhysicalAssetProperty<String> statusProperty = new PhysicalAssetProperty<>(STATUS_PROPERTY_KEY, STATUS_INACTIVE);
        final PhysicalAssetProperty<Position> positionProperty = new PhysicalAssetProperty<>(POSITION_PROPERTY_KEY,
                new Position(40.7059359, -73.99666));
        pad.getProperties().addAll(List.of(statusProperty, positionProperty));
        final PhysicalAssetAction setDestinationAction = new PhysicalAssetAction(SET_DESTINATION_ACTION_KEY, "destination.set", "");
        pad.getActions().add(setDestinationAction);

        try {
            this.notifyPhysicalAdapterBound(pad);
        } catch (PhysicalAdapterException | EventBusException e) {
            Logger.getLogger(AmbulancePhysicalAdapter.class.getName()).info(e.getMessage());
        }
    }

    @Override
    public void onAdapterStop() {

    }
}
