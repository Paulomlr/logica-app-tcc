/**
 * Rendering some operators as SVG and others as font glyphs meant the two
 * groups never quite matched in weight (font glyphs pick up the font's own
 * stroke boldness; hand-tuned SVG strokes didn't). Drawing every operator
 * as SVG, sharing one stroke-width, makes them consistent by construction -
 * and, same as before, immune to per-device font substitution.
 */
function AndIcon() {
  return (
    <svg className="op-icon op-and" viewBox="0 0 14 14" aria-hidden="true" focusable="false">
      <polyline points="1,12 7,2 13,12" />
    </svg>
  )
}

function OrIcon() {
  return (
    <svg className="op-icon op-or" viewBox="0 0 14 14" aria-hidden="true" focusable="false">
      <polyline points="1,2 7,12 13,2" />
    </svg>
  )
}

function NotIcon() {
  return (
    <svg className="op-icon op-not" viewBox="0 0 14 14" aria-hidden="true" focusable="false">
      <polyline points="1,3 13,3 13,13" />
    </svg>
  )
}

function ImpliesIcon() {
  return (
    <svg className="op-icon op-implies" viewBox="0 0 22 14" aria-hidden="true" focusable="false">
      <line x1="2" y1="7" x2="19" y2="7" />
      <polyline points="14,2 20,7 14,12" />
    </svg>
  )
}

function IffIcon() {
  return (
    <svg className="op-icon op-iff" viewBox="0 0 24 14" aria-hidden="true" focusable="false">
      <line x1="3" y1="7" x2="21" y2="7" />
      <polyline points="8,2 3,7 8,12" />
      <polyline points="16,2 21,7 16,12" />
    </svg>
  )
}

const OPERATOR_ICON: Record<string, typeof IffIcon> = {
  '∧': AndIcon,
  '∨': OrIcon,
  '¬': NotIcon,
  '→': ImpliesIcon,
  '↔': IffIcon,
}

function Formula({ text, className }: { text: string; className?: string }) {
  return (
    <span className={className}>
      {[...text].map((ch, i) => {
        const Icon = OPERATOR_ICON[ch]
        return Icon ? <Icon key={i} /> : ch
      })}
    </span>
  )
}

export default Formula
