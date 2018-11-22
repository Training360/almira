#!/bin/bash

ssh developer@tadev.training360.com '/opt/training/epbva/shutdown.sh'

echo Waiting for shutdown

ssh developer@tadev.training360.com 'grep -m 1 "shutdown" <(tail -f /opt/training/epbva/logs/console.log)'

scp target/ep-bva-0.0.1-SNAPSHOT.jar developer@tadev.training360.com:/opt/training/epbva

ssh developer@tadev.training360.com '/opt/training/epbva/startup.sh'

echo Waiting for startup

ssh developer@tadev.training360.com 'grep -m 1 "Started EpBvaApplication" <(tail -f /opt/training/epbva/logs/console.log)'