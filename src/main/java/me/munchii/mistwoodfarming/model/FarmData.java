package me.munchii.mistwoodfarming.model;

import java.util.Set;
import java.util.UUID;

public record FarmData(UUID id, String name, String regionName, UUID owner, Set<UUID> members) {
}
