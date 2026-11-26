# Feature Catalog

## Registry contract
The registry is the single source of feature metadata. UI renders catalog entries and configuration state; the runtime owns activation and emits status events. Never wire toggles directly to hook classes.

Each entry contains:
- stable ID (lowercase, namespaced)
- display title and description
- category
- default enabled state
- maturity (planned, experimental, stable, disabled)
- compatibility status
- settings schema (optional)
- runtime activation result

## Categories
- Feed: layout and feed experience customization
- Media: media handling and local interface improvements
- Interface: app appearance and interaction customization
- Utilities: convenience tools and local-only functions

## UX rules
- Search by title, description and category.
- Filter by enabled, disabled, experimental and incompatible.
- Feature detail shows purpose, state, compatibility and last activation result.
- Keep toggle states separate from activation status. Requested enabled does not imply successfully applied.
- Incompatible or unimplemented features cannot pretend to be active.
- Provide category sections with collapsible headers and counts.

## Runtime states
CONFIGURED, APPLYING, APPLIED, FAILED, SKIPPED, UNSUPPORTED, DISABLED.
