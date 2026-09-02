/**
 * ↔ and ¬ render inconsistently across devices - which fonts substitute
 * them, and at what size, depends on the OS (confirmed: fixing their size
 * by eye on one machine did not carry over to a phone with different system
 * fonts). Drawing them as SVG instead of relying on a font glyph makes them
 * render identically everywhere, sized in em so they still scale with
 * surrounding text.
 */
function IffIcon() {
  return (
    <svg className="op-icon op-iff" viewBox="0 0 24 14" aria-hidden="true" focusable="false">
      <line x1="3" y1="7" x2="21" y2="7" />
      <polyline points="8,2 3,7 8,12" />
      <polyline points="16,2 21,7 16,12" />
    </svg>
  )
}

function NotIcon() {
  return (
    <svg className="op-icon op-not" viewBox="0 0 14 12" aria-hidden="true" focusable="false">
      <polyline points="1,2 13,2 13,11" />
    </svg>
  )
}

const OPERATOR_ICON: Record<string, typeof IffIcon> = {
  '↔': IffIcon,
  '¬': NotIcon,
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
