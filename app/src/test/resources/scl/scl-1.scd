<?xml version="1.0" encoding="UTF-8"?>
<!-- SPDX-FileCopyrightText: 2020 RTE FRANCE -->
<!-- -->
<!-- SPDX-License-Identifier: Apache-2.0 -->

<SCL xmlns:somexy="http://www.iec.ch/61850/2003/SCLcoordinates" xmlns="http://www.iec.ch/61850/2003/SCL" xmlns:txy="http://www.iec.ch/61850/2003/Terminal" version="2007" revision="B">
    <Header id="TrainingIEC61850" version="1" revision="54" toolID="IEC 61850 System Configurator, Version: V5.80 " nameStructure="IEDName">
        <Text>TrainingIEC61850</Text>
        <History>
            <Hitem version="1" revision="1" when="Monday, July 30, 2018 5:24:07 PM" who="Licenced User: OMICRON electronics GmbH Information Systems Machine: JAKVOG00LW01 User: JakVog00" what="Device Relay_FIELDA is imported from C:\Users\JakVog00\AppData\Local\Temp\ey1oarkt.vmp\Relay_FIELDA.iid" why="IEC 61850 System Configurator Automatic Startup: Insert Comment"/>
        </History>
    </Header>
    <Substation name="AA1" desc="Substation">
        <Private type="ABBPCMInternalObjRef">0f6c4a8e-0904-4f41-9b01-03444ba9efc7</Private>
        <VoltageLevel somexy:x="1" somexy:y="3" name="J1" desc="Voltage Level">
            <Private type="ABBPCMInternalObjRef">b80686f1-a514-477b-a83b-78f1cbe8a582</Private>
            <Voltage unit="V" multiplier="k">20</Voltage>
            <Bay somexy:x="1" somexy:y="1" name="BusBar A">
                <ConnectivityNode pathName="AA1/J1/BusBar A/L1" name="L1"/>
            </Bay>
            <Bay somexy:x="1" somexy:y="2" name="BusBar B">
                <ConnectivityNode pathName="AA1/J1/BusBar B/L1" name="L1"/>
            </Bay>
            <Bay somexy:x="1" somexy:y="6" name="Bay A" desc="Feld A">
                <ConductingEquipment type="DIS" somexy:x="1" somexy:y="1" name="QB1">
                    <Terminal name="T1" connectivityNode="AA1/J1/BusBar B/L1" substationName="AA1" voltageLevelName="J1" bayName="BusBar B" cNodeName="L1" txy:actTerm="1"/>
                    <Terminal name="T3" connectivityNode="AA1/J1/Bay A/L1" substationName="AA1" voltageLevelName="J1" bayName="Bay A" cNodeName="L1" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="DIS" somexy:x="3" somexy:y="1" name="QB2">
                    <Terminal name="T1" connectivityNode="AA1/J1/BusBar A/L1" substationName="AA1" voltageLevelName="J1" bayName="BusBar A" cNodeName="L1" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/Bay A/L1" substationName="AA1" voltageLevelName="J1" bayName="Bay A" cNodeName="L1" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="CBR" somexy:x="2" somexy:y="3" name="QA1">
                    <Terminal name="T1" connectivityNode="AA1/J1/Bay A/L1" substationName="AA1" voltageLevelName="J1" bayName="Bay A" cNodeName="L1" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/Bay A/L2" substationName="AA1" voltageLevelName="J1" bayName="Bay A" cNodeName="L2" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="CTR" somexy:x="2" somexy:y="5" name="T1">
                    <Terminal name="T1" connectivityNode="AA1/J1/Bay A/L2" substationName="AA1" voltageLevelName="J1" bayName="Bay A" cNodeName="L2" txy:actTerm="1"/>
                    <Terminal name="T3" connectivityNode="AA1/J1/Bay A/L3" substationName="AA1" voltageLevelName="J1" bayName="Bay A" cNodeName="L3" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="VTR" somexy:x="3" somexy:y="7" name="T5">
                    <Terminal name="T11" connectivityNode="AA1/J1/Bay A/L3" substationName="AA1" voltageLevelName="J1" bayName="Bay A" cNodeName="L3" txy:actTerm="1"/>
                </ConductingEquipment>
                <ConductingEquipment type="DIS" somexy:x="1" somexy:y="7" name="QB9">
                    <Terminal name="T1" connectivityNode="AA1/J1/Bay A/L3" substationName="AA1" voltageLevelName="J1" bayName="Bay A" cNodeName="L3" txy:actTerm="1"/>
                </ConductingEquipment>
                <ConnectivityNode pathName="AA1/J1/Bay A/L1" name="L1"/>
                <ConnectivityNode pathName="AA1/J1/Bay A/L2" name="L2"/>
                <ConnectivityNode pathName="AA1/J1/Bay A/L3" name="L3"/>
            </Bay>
            <Bay somexy:x="6" somexy:y="6" name="Bay B" desc="Feld B">
                <ConductingEquipment type="DIS" somexy:x="1" somexy:y="1" name="QB1">
                    <Terminal name="T1" connectivityNode="AA1/J1/BusBar B/L1" substationName="AA1" voltageLevelName="J1" bayName="BusBar B" cNodeName="L1" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/Bay B/L1" substationName="AA1" voltageLevelName="J1" bayName="Bay B" cNodeName="L1" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="DIS" somexy:x="3" somexy:y="1" name="QB2">
                    <Terminal name="T1" connectivityNode="AA1/J1/BusBar A/L1" substationName="AA1" voltageLevelName="J1" bayName="BusBar A" cNodeName="L1" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/Bay B/L1" substationName="AA1" voltageLevelName="J1" bayName="Bay B" cNodeName="L1" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="CBR" somexy:x="2" somexy:y="3" name="QA1">
                    <Terminal name="T1" connectivityNode="AA1/J1/Bay B/L1" substationName="AA1" voltageLevelName="J1" bayName="Bay B" cNodeName="L1" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/Bay B/L2" substationName="AA1" voltageLevelName="J1" bayName="Bay B" cNodeName="L2" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="CTR" somexy:x="2" somexy:y="5" name="T1">
                    <Terminal name="T1" connectivityNode="AA1/J1/Bay B/L2" substationName="AA1" voltageLevelName="J1" bayName="Bay B" cNodeName="L2" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/Bay B/L3" substationName="AA1" voltageLevelName="J1" bayName="Bay B" cNodeName="L3" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="VTR" somexy:x="3" somexy:y="7" name="T5">
                    <Terminal name="T1" connectivityNode="AA1/J1/Bay B/L3" substationName="AA1" voltageLevelName="J1" bayName="Bay B" cNodeName="L3" txy:actTerm="1"/>
                </ConductingEquipment>
                <ConductingEquipment type="DIS" somexy:x="1" somexy:y="7" name="QB9">
                    <Terminal name="T1" connectivityNode="AA1/J1/Bay B/L3" substationName="AA1" voltageLevelName="J1" bayName="Bay B" cNodeName="L3" txy:actTerm="1"/>
                </ConductingEquipment>
                <ConnectivityNode pathName="AA1/J1/Bay B/L1" name="L1"/>
                <ConnectivityNode pathName="AA1/J1/Bay B/L2" name="L2"/>
                <ConnectivityNode pathName="AA1/J1/Bay B/L3" name="L3"/>
            </Bay>
            <Bay somexy:x="12" somexy:y="6" name="CoupField">
                <ConductingEquipment type="DIS" somexy:x="1" somexy:y="1" name="QB1">
                    <Terminal name="T1" connectivityNode="AA1/J1/BusBar B/L1" substationName="AA1" voltageLevelName="J1" bayName="BusBar B" cNodeName="L1" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/CoupField/L1" substationName="AA1" voltageLevelName="J1" bayName="CoupField" cNodeName="L1" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="CBR" somexy:x="2" somexy:y="3" somexy:dir="west" name="QA1">
                    <Terminal name="T1" connectivityNode="AA1/J1/CoupField/L1" substationName="AA1" voltageLevelName="J1" bayName="CoupField" cNodeName="L1" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/CoupField/L2" substationName="AA1" voltageLevelName="J1" bayName="CoupField" cNodeName="L2" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="DIS" somexy:x="3" somexy:y="1" name="QB2">
                    <LNode iedName="Relay_FIELDC" ldInst="Disc_QB1" prefix="" lnClass="XSWI" lnInst="1"/>
                    <LNode iedName="Relay_FIELDC" ldInst="Disc_QB1" prefix="" lnClass="CSWI" lnInst="1"/>
                    <LNode iedName="Relay_FIELDC" ldInst="Disc_QB1" prefix="" lnClass="CILO" lnInst="1"/>
                    <Terminal name="T1" connectivityNode="AA1/J1/BusBar A/L1" substationName="AA1" voltageLevelName="J1" bayName="BusBar A" cNodeName="L1" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/CoupField/L2" substationName="AA1" voltageLevelName="J1" bayName="CoupField" cNodeName="L2" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="DIS" somexy:x="4" somexy:y="1" name="QC11">
                    <Terminal name="T2" cNodeName="grounded"/>
                    <Terminal name="T1" connectivityNode="AA1/J1/BusBar B/L1" substationName="AA1" voltageLevelName="J1" bayName="BusBar B" cNodeName="L1" txy:actTerm="1"/>
                </ConductingEquipment>
                <ConductingEquipment type="DIS" somexy:x="5" somexy:y="1" name="QC12">
                    <Terminal name="T2" cNodeName="grounded"/>
                    <Terminal name="T1" connectivityNode="AA1/J1/BusBar A/L1" substationName="AA1" voltageLevelName="J1" bayName="BusBar A" cNodeName="L1" txy:actTerm="1"/>
                </ConductingEquipment>
                <ConnectivityNode pathName="AA1/J1/CoupField/L1" name="L1"/>
                <ConnectivityNode pathName="AA1/J1/CoupField/L2" name="L2"/>
            </Bay>
            <Bay somexy:x="19" somexy:y="6" name="Bay C" desc="Feld C">
                <ConductingEquipment type="DIS" somexy:x="1" somexy:y="1" name="QB1">
                    <Terminal name="T1" connectivityNode="AA1/J1/BusBar B/L1" substationName="AA1" voltageLevelName="J1" bayName="BusBar B" cNodeName="L1" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/Bay C/L1" substationName="AA1" voltageLevelName="J1" bayName="Bay C" cNodeName="L1" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="DIS" somexy:x="3" somexy:y="1" name="QB2">
                    <Terminal name="T1" connectivityNode="AA1/J1/BusBar A/L1" substationName="AA1" voltageLevelName="J1" bayName="BusBar A" cNodeName="L1" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/Bay C/L1" substationName="AA1" voltageLevelName="J1" bayName="Bay C" cNodeName="L1" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="CBR" somexy:x="2" somexy:y="3" name="QA1">
                    <Terminal name="T1" connectivityNode="AA1/J1/Bay C/L1" substationName="AA1" voltageLevelName="J1" bayName="Bay C" cNodeName="L1" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/Bay C/L2" substationName="AA1" voltageLevelName="J1" bayName="Bay C" cNodeName="L2" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="CTR" somexy:x="2" somexy:y="5" name="T1">
                    <Terminal name="T1" connectivityNode="AA1/J1/Bay C/L2" substationName="AA1" voltageLevelName="J1" bayName="Bay C" cNodeName="L2" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/Bay C/L3" substationName="AA1" voltageLevelName="J1" bayName="Bay C" cNodeName="L3" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="VTR" somexy:x="3" somexy:y="7" name="T5">
                    <Terminal name="T1" connectivityNode="AA1/J1/Bay C/L3" substationName="AA1" voltageLevelName="J1" bayName="Bay C" cNodeName="L3" txy:actTerm="1"/>
                </ConductingEquipment>
                <ConductingEquipment type="DIS" somexy:x="1" somexy:y="7" name="QB9">
                    <Terminal name="T1" connectivityNode="AA1/J1/Bay C/L3" substationName="AA1" voltageLevelName="J1" bayName="Bay C" cNodeName="L3" txy:actTerm="1"/>
                </ConductingEquipment>
                <ConnectivityNode pathName="AA1/J1/Bay C/L1" name="L1"/>
                <ConnectivityNode pathName="AA1/J1/Bay C/L2" name="L2"/>
                <ConnectivityNode pathName="AA1/J1/Bay C/L3" name="L3"/>
            </Bay>
            <Bay somexy:x="25" somexy:y="6" name="Bay D" desc="Feld D">
                <ConductingEquipment type="DIS" somexy:x="1" somexy:y="1" name="QB1">
                    <Terminal name="T1" connectivityNode="AA1/J1/BusBar B/L1" substationName="AA1" voltageLevelName="J1" bayName="BusBar B" cNodeName="L1" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/Bay D/L1" substationName="AA1" voltageLevelName="J1" bayName="Bay D" cNodeName="L1" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="DIS" somexy:x="3" somexy:y="1" name="QB2">
                    <Terminal name="T1" connectivityNode="AA1/J1/BusBar A/L1" substationName="AA1" voltageLevelName="J1" bayName="BusBar A" cNodeName="L1" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/Bay D/L1" substationName="AA1" voltageLevelName="J1" bayName="Bay D" cNodeName="L1" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="CBR" somexy:x="2" somexy:y="3" name="QA1">
                    <Terminal name="T1" connectivityNode="AA1/J1/Bay D/L1" substationName="AA1" voltageLevelName="J1" bayName="Bay D" cNodeName="L1" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/Bay D/L2" substationName="AA1" voltageLevelName="J1" bayName="Bay D" cNodeName="L2" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="CTR" somexy:x="2" somexy:y="5" name="T1">
                    <Terminal name="T1" connectivityNode="AA1/J1/Bay D/L2" substationName="AA1" voltageLevelName="J1" bayName="Bay D" cNodeName="L2" txy:actTerm="1"/>
                    <Terminal name="T2" connectivityNode="AA1/J1/Bay D/L3" substationName="AA1" voltageLevelName="J1" bayName="Bay D" cNodeName="L3" txy:actTerm="2"/>
                </ConductingEquipment>
                <ConductingEquipment type="DIS" somexy:x="1" somexy:y="7" name="QB9">
                    <Terminal name="T1" connectivityNode="AA1/J1/Bay D/L3" substationName="AA1" voltageLevelName="J1" bayName="Bay D" cNodeName="L3" txy:actTerm="1"/>
                </ConductingEquipment>
                <ConductingEquipment type="VTR" somexy:x="3" somexy:y="7" name="T5">
                    <Terminal name="T1" connectivityNode="AA1/J1/Bay D/L3" substationName="AA1" voltageLevelName="J1" bayName="Bay D" cNodeName="L3" txy:actTerm="1"/>
                </ConductingEquipment>
                <ConnectivityNode pathName="AA1/J1/Bay D/L1" name="L1"/>
                <ConnectivityNode pathName="AA1/J1/Bay D/L2" name="L2"/>
                <ConnectivityNode pathName="AA1/J1/Bay D/L3" name="L3"/>
            </Bay>
        </VoltageLevel>
    </Substation>
</SCL>
