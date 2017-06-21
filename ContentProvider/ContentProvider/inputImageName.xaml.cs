using System;
using System.IO.Compression;
using System.Windows;

namespace ContentProvider
{
    /// <summary>
    /// inputImageName.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class inputImageName : Window
    {
        private void ScreenCapture(int intBitmapWidth, int intBitmapHeight, System.Drawing.Point ptSource, string location)
        {
            System.Drawing.Bitmap bitmap = new System.Drawing.Bitmap(intBitmapWidth, intBitmapHeight);

            System.Drawing.Graphics g = System.Drawing.Graphics.FromImage(bitmap);

            g.CopyFromScreen(ptSource, new System.Drawing.Point(0, 0), new System.Drawing.Size(intBitmapWidth, intBitmapHeight));

            bitmap.Save(location, System.Drawing.Imaging.ImageFormat.Png);
        }

        public inputImageName()
        {
            InitializeComponent();
        }

        private void button_Click(object sender, RoutedEventArgs e)
        {
            MainWindow mw = ((MainWindow)(Application.Current.MainWindow));
            MainWindow.saveFileName = textBox.Text;

            this.Close();

            if (MainWindow.saveFileName != null)
            {
                string zipPath = mw.fileFullName;
                string extractPath = @"C:\extract";
                //string startPath = @"C:\extract";

                try
                {
                    

                    ZipFile.ExtractToDirectory(zipPath, extractPath);

                    ScreenCapture((int)mw.myInkCanvas.ActualWidth, (int)mw.myInkCanvas.ActualHeight,
                      (new System.Drawing.Point((int)Application.Current.MainWindow.Left + (int)mw.Width / 2,
                      (int)Application.Current.MainWindow.Top + (int)mw.button_back.ActualHeight * 5)),
                      @"C:\extract\OEBPS\Images\" + MainWindow.saveFileName + ".png");

                    /*
                    epub = null;
                    GC.Collect();
                    GC.WaitForPendingFinalizers();

                    deleteFile(fileFullName);
                    */

                    //ZipFile.CreateFromDirectory(startPath, @"C:\Users\kevin\Desktop\test.epub");

                    //deleteDirectory(extractPath);
                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.ToString());
                }
            }
        }
    }
}
