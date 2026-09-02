/**
 * IBM Plex Mono draws ↔ noticeably smaller than the other logic symbols
 * (∧, ∨, ¬, →) even though they all occupy the same character width - a
 * font design inconsistency, not a CSS sizing issue. Wrap it to visually
 * match the rest wherever a formula or operator legend is rendered as text.
 */
function Formula({ text, className }: { text: string; className?: string }) {
  return (
    <span className={className}>
      {[...text].map((ch, i) => (ch === '↔' ? <span key={i} className="op-iff">↔</span> : ch))}
    </span>
  )
}

export default Formula
