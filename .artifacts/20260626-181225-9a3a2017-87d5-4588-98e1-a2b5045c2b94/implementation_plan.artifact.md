# Implementation Plan - SpaceIA Mobile Sprint 5: Turn-by-Turn Navigation

Transform the static route visualization into an intelligent, interactive pedestrian navigation system for the UTL campus.

## User Review Required

> [!IMPORTANT]
> - **Deviation Detection**: I will use a cross-track distance algorithm (distance from point to segment) to detect when the user is > 15m away from the path to trigger a reroute.
> - **Instruction Generation**: Bearings will be calculated between consecutive segments (AB and BC) to determine turn types (LEFT, RIGHT, STRAIGHT).
> - **Smart Camera**: The camera will auto-rotate and tilt based on the user's current heading and segment direction.
> - **Feedback**: Vibration will be triggered via the `Vibrator` service for turns and arrival.

## Proposed Changes

### Domain Layer (`domain/navigation/`)

#### [NavigationInstructions.kt](file:///C:/Proyectos-Universidad/mobile-space-ai/app/src/main/java/org/utl/idgs901/space_ai_mobile/domain/navigation/model/NavigationInstructions.kt) [NEW]
- `NavigationInstruction` data class (type, text, distance, node).
- `InstructionType` enum (START, STRAIGHT, LEFT, RIGHT, ARRIVED).

#### [ActiveNavigation.kt](file:///C:/Proyectos-Universidad/mobile-space-ai/app/src/main/java/org/utl/idgs901/space_ai_mobile/domain/navigation/model/ActiveNavigation.kt) [NEW]
- State holder for the current navigation session.

#### [Navigation Use Cases](file:///C:/Proyectos-Universidad/mobile-space-ai/app/src/main/java/org/utl/idgs901/space_ai_mobile/domain/navigation/usecase/) [NEW]
- `StartNavigationUseCase`, `StopNavigationUseCase`, `UpdateNavigationUseCase`.

---

### Data Layer (`data/navigation/`)

#### [InstructionGenerator.kt](file:///C:/Proyectos-Universidad/mobile-space-ai/app/src/main/java/org/utl/idgs901/space_ai_mobile/data/navigation/instruction/InstructionGenerator.kt) [NEW]
- Computes human-readable steps and detects turns using bearing math.

#### [CurrentRouteTracker.kt](file:///C:/Proyectos-Universidad/mobile-space-ai/app/src/main/java/org/utl/idgs901/space_ai_mobile/data/navigation/tracker/CurrentRouteTracker.kt) [NEW]
- Matches GPS to route segments and calculates remaining metrics.

#### [RouteRecalculator.kt](file:///C:/Proyectos-Universidad/mobile-space-ai/app/src/main/java/org/utl/idgs901/space_ai_mobile/data/navigation/tracker/RouteRecalculator.kt) [NEW]
- Logic to trigger A* when off-route.

#### [CampusNavigationEngine.kt](file:///C:/Proyectos-Universidad/mobile-space-ai/app/src/main/java/org/utl/idgs901/space_ai_mobile/data/navigation/navigation/CampusNavigationEngine.kt) [NEW]
- Orchestrates tracking, instructions, and state transitions.

---

### Presentation Layer (`presentation/navigation/`)

#### [NavigationSessionViewModel.kt](file:///C:/Proyectos-Universidad/mobile-space-ai/app/src/main/java/org/utl/idgs901/space_ai_mobile/presentation/navigation/NavigationSessionViewModel.kt) [NEW]
- Manages the active navigation session and exposes `NavigationSessionUiState`.

#### [NavigationComponents.kt](file:///C:/Proyectos-Universidad/mobile-space-ai/app/src/main/java/org/utl/idgs901/space_ai_mobile/presentation/navigation/components/NavigationComponents.kt) [NEW]
- `NavigationInstructionCard`: Premium M3 card with turn icons.
- `NavigationDashboard`: Bottom UI with progress metrics.

#### [CampusMapScreen.kt](file:///C:/Proyectos-Universidad/mobile-space-ai/app/src/main/java/org/utl/idgs901/space_ai_mobile/presentation/map/CampusMapScreen.kt) [UPDATE]
- Integrate `NavigationSessionViewModel`.
- Overlay instruction cards and dashboards during active navigation.
- Implement `NavigationCameraController` for smooth user following.

---

### Core & Infrastructure

#### [NavigationSessionModule.kt](file:///C:/Proyectos-Universidad/mobile-space-ai/app/src/main/java/org/utl/idgs901/space_ai_mobile/core/di/NavigationSessionModule.kt) [NEW]
- DI for engine, generator, and trackers.

## Verification Plan

### Automated Tests
- `InstructionGeneratorTest`: Verify turn detection (90° left = LEFT, 0° = STRAIGHT).
- `CurrentRouteTrackerTest`: Test distance-to-polyline calculation and segment matching.
- `ArrivalDetectionTest`: Verify arrival trigger at < 5m.

### Manual Verification
1. **Reroute**: Simulate GPS moving 20m away from the path and verify new route generation.
2. **Turn Instructions**: Walk the path (or simulate) and check if icons/text update correctly.
3. **Camera**: Verify the map rotates to align with the user's current segment.
4. **Vibration**: Ensure physical feedback on instruction change (if supported by emulator/device).
5. **Arrival**: Check if the "Has llegado" state triggers correctly.
