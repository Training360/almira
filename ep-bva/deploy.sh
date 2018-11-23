#!/bin/bash

ssh developer@tadev.training360.com '/opt/training/epbva/shutdown.sh'

scp target/ep-bva-0.0.1-SNAPSHOT.jar developer@tadev.training360.com:/opt/training/epbva

ssh developer@tadev.training360.com '/opt/training/epbva/startup.sh'
