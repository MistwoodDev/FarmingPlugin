package me.munchii.mistwoodfarming.model;

import java.util.UUID;

public record PlayerData(String name, UUID farmId, String farmName, FarmPermissionLevel permissionLevel) {
}
