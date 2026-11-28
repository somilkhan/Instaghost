# UI Audit: Companion and in-app menu

Reviewed the current UI scaffold on the engineering branch. This is a static source review, not a device or accessibility validation.

## Strengths
- Monochrome, system-theme-aware styling with no third-party UI dependency.
- Planned features are visibly unavailable and switches are disabled unless implemented.
- Main companion has separate Home, Features, Health, and Settings sections.
- Basic tab accessibility metadata and focusability are present.

## Priority fixes
1. **Navigation refresh:** selected-tab state is rebuilt on every render. Verify focus restoration and screen-reader announcements; consider explicit selected-state semantics and stable navigation.
2. **Scrollable in-app menu:** fixed 380dp scroll viewport may be awkward on compact screens or landscape. Calculate a screen-bounded height and handle inset/keyboard constraints.
3. **Switch accessibility:** add descriptive summaries and ensure disabled states are announced; test focus order and contrast.
4. **Dialog lifecycle:** ensure show operations are idempotent and avoid duplicate dialogs during repeated Activity resume events.
5. **Blur fallback:** Android 12+ blur is optional. Validate fallback rendering and avoid depending on blur for contrast.
6. **Text scaling:** audit long feature descriptions at 130–200% font scale and narrow screens.
7. **Diagnostics screen:** replace static Health copy with local, privacy-safe runtime status and a clear/export flow only after state instrumentation exists.

## Validation required
- TalkBack navigation, font scaling, small phone, tablet/fold, landscape, light/dark.
- Android API 26 baseline and API 31+ blur path.
- Dialog dismissal and repeated launch/resume.
- No feature is marked verified based on static review alone.
