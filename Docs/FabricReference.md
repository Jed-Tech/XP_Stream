# Fabric Modding Reference (MC 1.21.11)

Quick reference for AI-assisted development. Tailored for XP_Stream but covers common patterns.

> **Verified**: December 2024 with Yarn 1.21.11+build.1, Fabric Loom 1.14.8, Gradle 9.2.1

---

## Table of Contents
1. [Mixin Fundamentals](#mixin-fundamentals)
2. [Injection Points](#injection-points)
3. [Callback Types](#callback-types)
4. [Entity & World APIs](#entity--world-apis)
5. [fabric.mod.json Schema](#fabricmodjson-schema)
6. [Mixin JSON Config](#mixin-json-config)
7. [Common Patterns](#common-patterns)
8. [Yarn Mappings Notes](#yarn-mappings-notes)
9. [Quick Reference: XP_Stream Specific](#quick-reference-xp_stream-specific)
10. [Gradle Commands](#gradle-commands)
11. [Toolchain Requirements (MC 1.21.11)](#toolchain-requirements-mc-12111)

---

## Mixin Fundamentals

### Basic Structure
```java
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TargetClass.class)
public abstract class TargetClassMixin {
    
    @Inject(method = "targetMethod", at = @At("HEAD"))
    private void modid$descriptiveName(CallbackInfo ci) {
        // Your code here
    }
}
```

### Key Annotations

| Annotation | Purpose |
|------------|---------|
| `@Mixin(TargetClass.class)` | Declares target class to modify |
| `@Inject` | Insert code at specific points |
| `@Redirect` | Replace a single method call within target |
| `@Overwrite` | Replace entire method (use sparingly!) |
| `@ModifyArg` | Change an argument passed to a method call |
| `@ModifyVariable` | Change a local variable's value |
| `@ModifyReturnValue` | Change what a method returns |
| `@Shadow` | Access private fields/methods from target class |
| `@Unique` | Mark fields/methods as added by your mixin |

### @Shadow - Accessing Target Class Members
```java
@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin {
    
    // Access private field (check Yarn mappings for exact name)
    @Shadow private int orbAge;
    
    // Access method via DataTracker (1.21.11 uses getValue() not getExperienceAmount())
    @Shadow public abstract int getValue();
}
```

### @Unique - Adding New Members
```java
@Mixin(SomeClass.class)
public class SomeClassMixin {
    
    // Unique field - won't conflict with other mixins
    @Unique
    private boolean modid$customFlag = false;
    
    // Unique method
    @Unique
    private void modid$helperMethod() {
        // ...
    }
}
```

---

## Injection Points

### @At Targets

| Value | Injects At |
|-------|------------|
| `"HEAD"` | Very start of method |
| `"TAIL"` | Before every return statement |
| `"RETURN"` | Before specific return (use `ordinal`) |
| `"INVOKE"` | Before/after a method call |
| `"FIELD"` | Before/after field access |
| `"NEW"` | Before object instantiation |

### INVOKE Examples
```java
// Inject before a specific method call within the target
@Inject(
    method = "onPlayerCollision",
    at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/entity/player/PlayerEntity;addExperience(I)V"
    )
)
private void beforeAddXp(PlayerEntity player, CallbackInfo ci) {
    // Runs right before player.addExperience() is called
}

// Inject AFTER that call
@Inject(
    method = "onPlayerCollision",
    at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/entity/player/PlayerEntity;addExperience(I)V",
        shift = At.Shift.AFTER
    )
)
private void afterAddXp(PlayerEntity player, CallbackInfo ci) {
    // Runs right after player.addExperience() returns
}
```

### Target Descriptor Format
```
L<fully/qualified/ClassName>;<methodName>(<paramDescriptors>)<returnDescriptor>
```
Type descriptors:
- `V` = void
- `I` = int
- `Z` = boolean
- `D` = double
- `F` = float
- `J` = long
- `[I` = int[]
- `Lpath/ClassName;` = object type

Examples:
- `()V` = void method, no params
- `(I)V` = void method, one int param
- `(Lnet/minecraft/entity/player/PlayerEntity;)V` = void, one PlayerEntity param
- `()I` = returns int, no params

### Ordinal - Targeting Specific Occurrences
```java
// Target the 2nd INVOKE of someMethod (0-indexed)
@At(value = "INVOKE", target = "...", ordinal = 1)
```

---

## Callback Types

### CallbackInfo (void methods)
```java
@Inject(method = "voidMethod", at = @At("HEAD"))
private void example(CallbackInfo ci) {
    // Cancel the original method:
    ci.cancel();  // Requires cancellable = true
}

// Must specify cancellable if you want to cancel
@Inject(method = "voidMethod", at = @At("HEAD"), cancellable = true)
private void exampleCancellable(CallbackInfo ci) {
    if (someCondition) {
        ci.cancel();
        return;
    }
}
```

### CallbackInfoReturnable<T> (methods with return values)
```java
@Inject(method = "getAmount", at = @At("HEAD"), cancellable = true)
private void modifyAmount(CallbackInfoReturnable<Integer> cir) {
    // Override return value and skip original
    cir.setReturnValue(42);
}

@Inject(method = "getAmount", at = @At("RETURN"))
private void afterGetAmount(CallbackInfoReturnable<Integer> cir) {
    int originalValue = cir.getReturnValue();
    // Can read but not modify at RETURN without cancellable
}

@Inject(method = "getAmount", at = @At("RETURN"), cancellable = true)
private void modifyReturnedAmount(CallbackInfoReturnable<Integer> cir) {
    cir.setReturnValue(cir.getReturnValue() * 2);
}
```

### Capturing Method Parameters
Parameters come before CallbackInfo in the mixin method signature:
```java
// Target: public void onPlayerCollision(PlayerEntity player)
@Inject(method = "onPlayerCollision", at = @At("HEAD"))
private void example(PlayerEntity player, CallbackInfo ci) {
    // 'player' is captured from the target method's parameter
}
```

### Capturing Locals
```java
@Inject(
    method = "someMethod",
    at = @At(value = "INVOKE", target = "..."),
    locals = LocalCapture.CAPTURE_FAILHARD
)
private void captureLocals(
    /* original params first */
    CallbackInfo ci,
    /* then locals in declaration order */
    int localVar1,
    String localVar2
) {
    // Access local variables at injection point
}
```

---

## Entity & World APIs

### Getting Nearby Entities (XP_Stream pattern)
```java
// Get entities within a bounding box
Box searchBox = player.getBoundingBox().expand(radiusBlocks);

// Method 1: getEntitiesByClass (simpler)
List<ExperienceOrbEntity> nearbyOrbs = world.getEntitiesByClass(
    ExperienceOrbEntity.class,
    searchBox,
    orb -> orb.isAlive() && orb != thisOrb  // predicate filter
);

// Method 2: getEntitiesByType with TypeFilter (1.21.11 pattern)
import net.minecraft.util.TypeFilter;

List<ExperienceOrbEntity> allOrbs = world.getEntitiesByType(
    TypeFilter.instanceOf(ExperienceOrbEntity.class),
    searchBox,
    orb -> orb.isAlive()
);
```

### Box (Bounding Box) Operations
```java
Box box = entity.getBoundingBox();
Box expanded = box.expand(1.5);              // Expand all directions
Box expanded2 = box.expand(1.0, 0.5, 1.0);   // Expand x, y, z separately
Box offset = box.offset(1.0, 0, 0);          // Move the box
boolean intersects = box.intersects(otherBox);
boolean contains = box.contains(x, y, z);
```

### World Checks
```java
// Check if server-side (important for mixins)
// NOTE: In 1.21.11 Yarn, use getEntityWorld() not getWorld()
if (!entity.getEntityWorld().isClient()) {
    // Server-side only code
}

// Better pattern - cast to ServerWorld
if (entity.getEntityWorld() instanceof ServerWorld serverWorld) {
    // Server-side operations
    serverWorld.spawnEntity(entity);
}
```

### ExperienceOrbEntity Key Methods (1.21.11 Yarn)
```java
// Get XP value - NOTE: getValue() not getExperienceAmount() in 1.21.11!
int xp = orb.getValue();

// Trigger pickup (this is what we want for Mending compatibility!)
orb.onPlayerCollision(player);

// Remove the orb
orb.discard();

// Check if already collected/dead
if (orb.isAlive()) { ... }
if (orb.isRemoved()) { ... }

// Internal: pickingCount tracks merged orb count
// Orb discards itself when pickingCount reaches 0 after pickup
```

### PlayerEntity XP Methods
```java
// Add XP directly (bypasses Mending!)
player.addExperience(amount);

// Add XP points (also bypasses Mending)
player.addExperiencePoints(amount);

// Get current XP
int level = player.experienceLevel;
float progress = player.experienceProgress;  // 0.0 to 1.0
int total = player.totalExperience;
```

> **XP_Stream Note**: To preserve Mending, always use `orb.onPlayerCollision(player)` 
> rather than `player.addExperience()`. The orb's collision method calls 
> `repairPlayerGears()` which uses `EnchantmentHelper.chooseEquipmentWith(EnchantmentEffectComponentTypes.REPAIR_WITH_XP, ...)`
> to handle Mending before awarding leftover XP.

---

## fabric.mod.json Schema

```json
{
  "schemaVersion": 1,
  "id": "mod_id",
  "version": "1.0.0",
  "name": "Display Name",
  "description": "Mod description",
  "authors": ["Author Name"],
  "contact": {
    "homepage": "https://...",
    "sources": "https://github.com/...",
    "issues": "https://github.com/.../issues"
  },
  "license": "MIT",
  "icon": "assets/mod_id/icon.png",
  "environment": "*",
  "entrypoints": {
    "main": ["com.example.mod.MainMod"],
    "client": ["com.example.mod.ClientMod"],
    "server": ["com.example.mod.ServerMod"]
  },
  "mixins": [
    "mod_id.mixins.json"
  ],
  "accessWidener": "mod_id.accesswidener",
  "depends": {
    "fabricloader": ">=0.17.0",
    "minecraft": "1.21.11",
    "java": ">=21"
  },
  "recommends": {
    "another-mod": "*"
  },
  "suggests": {},
  "breaks": {},
  "conflicts": {}
}
```

### Environment Values
| Value | Meaning |
|-------|---------|
| `"*"` | Loads on both client and server |
| `"client"` | Client-side only |
| `"server"` | Dedicated server only |

### Dependency Version Syntax
| Syntax | Meaning |
|--------|---------|
| `"*"` | Any version |
| `"1.0.0"` | Exactly 1.0.0 |
| `">=1.0.0"` | 1.0.0 or higher |
| `"~1.21"` | 1.21.x (any patch version) |
| `"1.21.x"` | Same as above |
| `">1.0.0 <2.0.0"` | Range |

---

## Mixin JSON Config

```json
{
  "required": true,
  "package": "com.example.mod.mixin",
  "compatibilityLevel": "JAVA_21",
  "mixins": [
    "CommonMixin"
  ],
  "client": [
    "ClientOnlyMixin"
  ],
  "server": [
    "ServerOnlyMixin"  
  ],
  "injectors": {
    "defaultRequire": 1
  },
  "refmap": "mod_id.refmap.json"
}
```

### Key Fields
| Field | Purpose |
|-------|---------|
| `required` | If true, game won't load if mixin fails |
| `package` | Base package for mixin classes |
| `compatibilityLevel` | Java version (JAVA_21 for MC 1.21+) |
| `mixins` | Common mixins (both sides) |
| `client` | Client-only mixins |
| `server` | Server-only mixins |
| `defaultRequire` | How many injection points must succeed (1 = all must succeed) |

---

## Common Patterns

### Re-entrancy Guard (used in XP_Stream)
```java
@Mixin(SomeClass.class)
public class SomeClassMixin {
    
    @Unique
    private static boolean modid$processing = false;
    
    @Inject(method = "targetMethod", at = @At("TAIL"))
    private void onTrigger(CallbackInfo ci) {
        if (modid$processing) return;
        
        try {
            modid$processing = true;
            // Do work that might trigger this method again
        } finally {
            modid$processing = false;
        }
    }
}
```

### Conditional Cancellation
```java
@Inject(method = "doThing", at = @At("HEAD"), cancellable = true)
private void maybeCancel(CallbackInfo ci) {
    if (shouldCancel()) {
        ci.cancel();
    }
    // Otherwise, original method runs normally
}
```

### Accessing 'this' in Mixin
```java
@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin {
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        // Cast 'this' to the target class
        ExperienceOrbEntity self = (ExperienceOrbEntity)(Object)this;
        
        // Now you can call methods on it
        int amount = self.getValue();  // NOTE: getValue() in 1.21.11
    }
}
```

### Extending Entity Classes
```java
@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin extends Entity {
    
    // Now you have direct access to Entity methods
    // But you need a dummy constructor:
    protected ExperienceOrbEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        World world = this.getEntityWorld();  // NOTE: getEntityWorld() in 1.21.11!
    }
}
```

### Server-Side Only Logic
```java
@Inject(method = "onPlayerCollision", at = @At("TAIL"))
private void afterCollision(PlayerEntity player, CallbackInfo ci) {
    // Pattern 1: Simple check (use getEntityWorld() in 1.21.11!)
    if (player.getEntityWorld().isClient()) return;
    
    // Pattern 2: Type check with smart cast (RECOMMENDED)
    if (!(player.getEntityWorld() instanceof ServerWorld serverWorld)) return;
    
    // Now use serverWorld...
}
```

---

## Yarn Mappings Notes

### Finding Method Names
Yarn mappings may differ between MC versions. To find the correct name:

1. Check the [Fabric Yarn repository](https://github.com/FabricMC/yarn)
2. Use the Linkie Discord bot
3. Check in-IDE with Ctrl+Click on class references
4. **Best**: Run `./gradlew :fabric:genSources` and extract from the sources JAR

### Confirmed 1.21.11 Mappings (Yarn 1.21.11+build.1)

| What | Correct Method | NOT This |
|------|---------------|----------|
| Entity world access | `getEntityWorld()` | ~~`getWorld()`~~ |
| XP orb value | `getValue()` | ~~`getExperienceAmount()`~~ |
| XP pickup hook | `onPlayerCollision(PlayerEntity)` | ✓ confirmed |

### Common Mapping Differences
For MC 1.21.11 with Yarn:
- `Entity.world` → `Entity.getEntityWorld()` (NOT `getWorld()`!)
- `ExperienceOrbEntity.amount` → accessed via `getValue()` (DataTracker)
- Some methods renamed between minor versions
- Check the specific Yarn version in gradle.properties

### Target Verification
If a mixin target isn't found:
1. Check `build.gradle` for correct Yarn mapping version
2. Run `./gradlew :fabric:genSources` to decompile MC source
3. Sources are in `~/.gradle/caches/fabric-loom/minecraftMaven/`
4. Extract and search the `*-sources.jar` for exact method names
5. Navigate to the class in your IDE to verify method names

---

## Quick Reference: XP_Stream Specific

### Key Classes
| Class | Purpose |
|-------|---------|
| `ExperienceOrbEntity` | The XP orb entity |
| `PlayerEntity` | Player (has XP methods) |
| `ServerWorld` | Server-side world |
| `ServerPlayerEntity` | Server-side player (for Mending repair) |
| `Box` | Bounding box for entity queries |
| `TypeFilter` | For `getEntitiesByType()` queries |

### Critical Method for Mending
```java
// CORRECT - Uses vanilla pickup path, Mending works
nearbyOrb.onPlayerCollision(player);

// WRONG - Bypasses Mending!
player.addExperience(nearbyOrb.getValue());  // Note: getValue() in 1.21.11
```

### Sound Suppression (if needed)
The vanilla `onPlayerCollision` does NOT play a sound directly - it calls
`player.sendPickup(this, 1)` which handles the pickup effect. The XP sound
is played elsewhere. For burst pickup:
1. Accept vanilla behavior (simplest)
2. Sound spam is unlikely since orbs picked up in same tick may coalesce
3. If needed, use a mixin to track/suppress during burst window

### ExperienceOrbEntity Internal Flow (1.21.11)
```
onPlayerCollision(player)
  └─ if (player instanceof ServerPlayerEntity)
       └─ repairPlayerGears(player, getValue())  // Mending first!
            └─ EnchantmentHelper.chooseEquipmentWith(REPAIR_WITH_XP, ...)
            └─ returns leftover XP
       └─ player.addExperience(leftover)
       └─ pickingCount--
       └─ if (pickingCount == 0) discard()
```

### XP_Stream Burst Pickup Implementation (v0.1)
```java
// After triggering orb is picked up (TAIL injection):
ExperienceOrbEntity self = (ExperienceOrbEntity)(Object)this;
Box playerBox = player.getBoundingBox();  // No expansion - collision-based

List<ExperienceOrbEntity> collidingOrbs = serverWorld.getEntitiesByType(
    TypeFilter.instanceOf(ExperienceOrbEntity.class),
    playerBox,
    orb -> orb.isAlive() && orb != self
);

for (ExperienceOrbEntity orb : collidingOrbs) {
    if (picked >= MAX_BURST_ORBS) break;
    player.experiencePickUpDelay = 0;  // Critical: bypass delay gate
    orb.onPlayerCollision(player);     // Vanilla path - Mending works
    picked++;
}
```

### v0.1 Test Results
| Metric | Vanilla | XP_Stream | Improvement |
|--------|---------|-----------|-------------|
| Total Time (200 orbs) | 20.86s | 11.06s | **1.89x faster** |
| Absorption Time | ~10.86s | ~1.06s | **~10x faster** |
| XP Integrity | ✅ | ✅ | Identical |

---

## Gradle Commands

```bash
# Generate Minecraft sources (for IDE navigation)
./gradlew :fabric:genSources

# Build the mod
./gradlew build

# Run client
./gradlew runClient

# Run server  
./gradlew runServer

# Clean build
./gradlew clean build

# Check dependencies
./gradlew dependencies

# Refresh dependencies (if versions changed)
./gradlew build --refresh-dependencies
```

---

## Toolchain Requirements (MC 1.21.11)

### Version Matrix (Confirmed Working)
| Component | Version | Notes |
|-----------|---------|-------|
| Minecraft | 1.21.11 | New versioning scheme (not 1.21.1.1) |
| Gradle | **9.2.1** | Required for Loom 1.14 |
| Fabric Loom | **1.14-SNAPSHOT** | Resolves to 1.14.8; needed for Unpick V4 |
| Fabric Loader | 0.17.2 | |
| Yarn Mappings | 1.21.11+build.1 | |
| Java | 21 | JDK 21 required |

### Gradle 9.x Breaking Changes
```groovy
// OLD (Gradle 8.x) - BROKEN in 9.x
archivesBaseName = "mymod"

// NEW (Gradle 9.x) - REQUIRED
base {
    archivesName = "mymod"
}
```

### Required settings.gradle
```groovy
pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = "https://maven.fabricmc.net/"
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
```

### Why Loom 1.14?
- Loom 1.12+ added Unpick V4 support
- Yarn mappings for 1.21.11 use Unpick V4
- Using older Loom versions causes: `UnsupportedOperationException: Unsupported unpick version`

---

*Last updated: December 2024 for Minecraft 1.21.11 / Fabric Loader 0.17.2+ / Loom 1.14.8 / Gradle 9.2.1*
*XP_Stream v0.1 implementation verified and tested*
