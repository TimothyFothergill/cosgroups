package utility

trait Mapping[A, B] {
  def map(a: A): B
}

trait MultiMapping[A, B, C] {
  def map(a: A, b: B): C
}

trait MultiMappingWithSeq[A, B, C] {
  def map(a: A, b: Seq[B]): C
}

trait ManyMappingWithSeq[A, B, C, D] {
  def map(a: A, b: Seq[B], c: Seq[C]): D
}
