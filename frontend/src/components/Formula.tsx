const OPERATOR_CLASS: Record<string, string> = {
  '↔': 'op-iff',
  '¬': 'op-not',
}

/**
 * IBM Plex Mono draws ↔ and ¬ noticeably smaller than ∧, ∨, → (measured via
 * canvas pixel bounds: same advance width, much shorter ink height) - a font
 * design inconsistency, not a CSS sizing issue. Wrap them to visually match
 * the rest wherever a formula or operator legend is rendered as text.
 */
function Formula({ text, className }: { text: string; className?: string }) {
  return (
    <span className={className}>
      {[...text].map((ch, i) => {
        const opClass = OPERATOR_CLASS[ch]
        return opClass ? (
          <span key={i} className={opClass}>
            {ch}
          </span>
        ) : (
          ch
        )
      })}
    </span>
  )
}

export default Formula
