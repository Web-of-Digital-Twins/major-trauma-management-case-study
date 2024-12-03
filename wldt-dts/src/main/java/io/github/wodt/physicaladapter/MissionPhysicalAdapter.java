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

import it.wldt.adapter.physical.PhysicalAdapter;
import it.wldt.adapter.physical.PhysicalAssetDescription;
import it.wldt.adapter.physical.PhysicalAssetProperty;
import it.wldt.adapter.physical.PhysicalAssetRelationship;
import it.wldt.adapter.physical.event.PhysicalAssetActionWldtEvent;
import it.wldt.adapter.physical.event.PhysicalAssetPropertyWldtEvent;
import it.wldt.adapter.physical.event.PhysicalAssetRelationshipInstanceCreatedWldtEvent;
import it.wldt.exception.EventBusException;
import it.wldt.exception.PhysicalAdapterException;

import java.util.List;
import java.util.logging.Logger;

/** Mission Physical Adapter. */
public class MissionPhysicalAdapter extends PhysicalAdapter {
    private static final String STATUS_PROPERTY_KEY = "status";
    private static final String VEHICLE_RELATIONSHIP_KEY = "vehicle";
    private static final String RESCUER_RELATIONSHIP_KEY = "rescuer";
    private static final String PATIENT_RELATIONSHIP_KEY = "patient";
    private static final String PLANNED_STATUS = "planned";
    private static final String MOVING_STATUS = "on-hold";
    private static final String AT_PLACE_STATUS = "in-progress";

    private final PhysicalAssetRelationship<String> vehicleRelationship =
            new PhysicalAssetRelationship<>(VEHICLE_RELATIONSHIP_KEY);
    private final PhysicalAssetRelationship<String> rescuerRelationship =
            new PhysicalAssetRelationship<>(RESCUER_RELATIONSHIP_KEY);
    private final PhysicalAssetRelationship<String> patientRelationship =
            new PhysicalAssetRelationship<>(PATIENT_RELATIONSHIP_KEY);

    private final String ambulanceUri;
    private final String rescuerUri;
    private final String patientUri;

    /**
     * Default constructor.
     */
    public MissionPhysicalAdapter(final String ambulanceUri, final String rescuerUri, final String patientUri) {
        super("mission-physical-adapter");
        this.ambulanceUri = ambulanceUri;
        this.rescuerUri = rescuerUri;
        this.patientUri = patientUri;
    }

    @Override
    public void onIncomingPhysicalAction(final PhysicalAssetActionWldtEvent<?> physicalActionEvent) {}

    @Override
    public void onAdapterStart() {
        final PhysicalAssetDescription pad = new PhysicalAssetDescription();
        final PhysicalAssetProperty<String> statusProperty = new PhysicalAssetProperty<>(STATUS_PROPERTY_KEY, PLANNED_STATUS);
        pad.getProperties().add(statusProperty);
        pad.getRelationships().addAll(List.of(vehicleRelationship, rescuerRelationship, patientRelationship));

        try {
            this.notifyPhysicalAdapterBound(pad);
        } catch (PhysicalAdapterException | EventBusException e) {
            Logger.getLogger(MissionPhysicalAdapter.class.getName()).info(e.getMessage());
        }

        new Thread(this::emulatedDevice).start();
    }

    @Override
    public void onAdapterStop() {

    }

    private void emulatedDevice() {
        try {
            Thread.sleep(20000);

            final var ambulanceInstance = vehicleRelationship.createRelationshipInstance(this.ambulanceUri);
            publishPhysicalAssetRelationshipCreatedWldtEvent(
                    new PhysicalAssetRelationshipInstanceCreatedWldtEvent<>(
                            ambulanceInstance
                    )
            );
            final var rescueInstance = rescuerRelationship.createRelationshipInstance(this.rescuerUri);
            publishPhysicalAssetRelationshipCreatedWldtEvent(
                    new PhysicalAssetRelationshipInstanceCreatedWldtEvent<>(
                            rescueInstance
                    )
            );
            // Turn status into moving
            final PhysicalAssetPropertyWldtEvent<String> newMovingStatus = new PhysicalAssetPropertyWldtEvent<>(
                    STATUS_PROPERTY_KEY,
                    MOVING_STATUS
            );
            publishPhysicalAssetPropertyWldtEvent(newMovingStatus);

            Thread.sleep(5000);

            final var patientInstance = patientRelationship.createRelationshipInstance(patientUri);
            publishPhysicalAssetRelationshipCreatedWldtEvent(
                    new PhysicalAssetRelationshipInstanceCreatedWldtEvent<>(
                            patientInstance
                    )
            );

            // Turn status into at-place
            final PhysicalAssetPropertyWldtEvent<String> newAtPlaceStatus = new PhysicalAssetPropertyWldtEvent<>(
                    STATUS_PROPERTY_KEY,
                    AT_PLACE_STATUS
            );
            publishPhysicalAssetPropertyWldtEvent(newAtPlaceStatus);
        } catch (EventBusException | InterruptedException e) {
            Logger.getLogger(MissionPhysicalAdapter.class.getName()).info(e.getMessage());
        }
    }
}
