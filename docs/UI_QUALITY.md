# UI quality and interaction principles

The companion app should remain useful even before runtime hooks are validated.

## Navigation
- Keep four destinations at most in the primary navigation. Home summarizes state, Features lists feature cards, Health reports runtime diagnostics, Settings controls app preferences.
- Use consistent spacing, type scale, and card styles. Avoid glass effects that reduce contrast or accessibility.
- Preserve navigation state on rotation and process recreation where possible.

## Feature rows
- Each row needs a concise name, plain-language explanation, category, availability state, and toggle only if implementation and validation gates have passed.
- Planned features must not present disabled-looking switches as if they were functional. Use a clear "Planned" badge and no toggle.
- Feature enablement should persist, but changes should be applied only after runtime hook support is verified.

## Diagnostics
- Show status as a clear state: Ready, Partial, Unavailable, or Error.
- Present sanitized event categories and time, not stack traces, Instagram content, account details, or identifiers.
- Provide a clear way to clear local diagnostic history.

## Accessibility and visual polish
- Use minimum 48dp touch targets.
- Support system font scaling and dark/light themes.
- Avoid color-only state indicators; pair them with text and iconography.
- Provide accessibility labels and selected state for navigation.
- Keep motion optional and restrained.

## Acceptance checks
- [ ] Screen reader can identify tabs, buttons, status, and feature availability.
- [ ] Large fonts do not clip labels or hide actions.
- [ ] Empty diagnostics state is understandable.
- [ ] Planned features cannot be mistaken for active controls.
- [ ] Navigation and settings remain usable on narrow screens.
