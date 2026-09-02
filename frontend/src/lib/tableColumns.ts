import type { CSSProperties } from 'react'
import type { ExercisePlayView } from './api'

export const GIVEN_COL_WIDTH = 32

/**
 * Column order from the backend interleaves "given" (variable) columns among
 * computed ones wherever they first appear in the formula, so it can't be
 * relied on to keep them together. This reorders for display only, moving
 * variables to a contiguous, alphabetically sorted prefix, so they can be
 * pinned to the left of the table while the computed columns scroll under
 * them. Row/answer lookups still use the original column index.
 */
export function getDisplayColumns(play: ExercisePlayView) {
  const givenIndexes = play.variables
    .map((v) => play.columnLabels.indexOf(v))
    .filter((i) => i !== -1)
  const fillableIndexes = play.columnLabels
    .map((_, i) => i)
    .filter((i) => play.columnIsFillable[i])
  return { order: [...givenIndexes, ...fillableIndexes], givenCount: givenIndexes.length }
}

export function pinnedColumnProps(pos: number, givenCount: number, extraClass?: string) {
  const isGiven = pos < givenCount
  const classes = [extraClass]
  let style: CSSProperties | undefined

  if (isGiven) {
    classes.push('given-col')
    if (pos === givenCount - 1) classes.push('given-col-last')
    style = { left: pos * GIVEN_COL_WIDTH, width: GIVEN_COL_WIDTH, minWidth: GIVEN_COL_WIDTH }
  }

  return { className: classes.filter(Boolean).join(' ') || undefined, style }
}
