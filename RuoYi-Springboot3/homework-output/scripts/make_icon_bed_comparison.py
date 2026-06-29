from pathlib import Path

from PIL import Image, ImageDraw, ImageFont


ROOT = Path(__file__).resolve().parents[2]
SCREENSHOTS = ROOT / "homework-output" / "screenshots"
OUTPUT = SCREENSHOTS / "icon-bed-design-comparison.png"

PAIRS = [
    (
        "Monitoring menu icons",
        Path(r"C:\Users\青峰不语\AppData\Local\Temp\codex-clipboard-94d0efce-a4d9-4953-9c00-ec52e05eecdd.png"),
        SCREENSHOTS / "defense-menu-icons-fixed.png",
        (0, 370, 185, 580),
    ),
    (
        "Responsible elder icon",
        Path(r"C:\Users\青峰不语\AppData\Local\Temp\codex-clipboard-8cb2dd37-9bdb-4d85-b7fa-c361ef1f0804.png"),
        SCREENSHOTS / "defense-responsible-icon-fixed.png",
        (0, 245, 185, 320),
    ),
    (
        "Bed status legend",
        Path(r"C:\Users\青峰不语\AppData\Local\Temp\codex-clipboard-9361e2ba-858c-49fc-a730-2a463b2370d7.png"),
        SCREENSHOTS / "defense-bed-legend-fixed.png",
        (190, 60, 555, 180),
    ),
    (
        "Room and bed status editor",
        Path(r"C:\Users\青峰不语\AppData\Local\Temp\codex-clipboard-9541d56c-6ac3-40dd-bba2-bf8f0ceb099f.png"),
        SCREENSHOTS / "defense-bed-status-edit.png",
        (375, 50, 1065, 425),
    ),
]

PANEL_WIDTH = 680
PANEL_HEIGHT = 330
HEADER_HEIGHT = 36
PADDING = 22
GAP = 18


def fit(image: Image.Image) -> Image.Image:
    image = image.copy()
    image.thumbnail((PANEL_WIDTH - 12, PANEL_HEIGHT - 12), Image.Resampling.LANCZOS)
    return image


def paste_center(canvas: Image.Image, image: Image.Image, left: int, top: int) -> None:
    x = left + (PANEL_WIDTH - image.width) // 2
    y = top + (PANEL_HEIGHT - image.height) // 2
    canvas.paste(image, (x, y))


def main() -> None:
    width = PADDING * 2 + PANEL_WIDTH * 2 + GAP
    row_height = HEADER_HEIGHT + PANEL_HEIGHT
    height = PADDING * 2 + len(PAIRS) * row_height + (len(PAIRS) - 1) * GAP
    canvas = Image.new("RGB", (width, height), "#eef1f5")
    draw = ImageDraw.Draw(canvas)
    font = ImageFont.load_default()

    for index, (title, source_path, implementation_path, implementation_crop) in enumerate(PAIRS):
        y = PADDING + index * (row_height + GAP)
        draw.text((PADDING, y + 10), f"{title} - reported", fill="#263445", font=font)
        draw.text((PADDING + PANEL_WIDTH + GAP, y + 10), f"{title} - fixed", fill="#263445", font=font)

        source = Image.open(source_path).convert("RGB")
        implementation = Image.open(implementation_path).convert("RGB").crop(implementation_crop)
        left = PADDING
        right = PADDING + PANEL_WIDTH + GAP
        top = y + HEADER_HEIGHT
        draw.rectangle((left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT), fill="white", outline="#c8cfd8")
        draw.rectangle((right, top, right + PANEL_WIDTH, top + PANEL_HEIGHT), fill="white", outline="#c8cfd8")
        paste_center(canvas, fit(source), left, top)
        paste_center(canvas, fit(implementation), right, top)

    canvas.save(OUTPUT, quality=94)
    print(OUTPUT)


if __name__ == "__main__":
    main()
